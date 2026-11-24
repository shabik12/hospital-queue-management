package com.hospital.hospital_queue_management.service.imp;


import com.hospital.hospital_queue_management.dto.request.PrescriptionMedicineRequest;
import com.hospital.hospital_queue_management.dto.request.PrescriptionRequest;
import com.hospital.hospital_queue_management.dto.response.PharmacyPrescriptionMedicineResponse;
import com.hospital.hospital_queue_management.dto.response.PharmacyPrescriptionResponse;
import com.hospital.hospital_queue_management.dto.response.PrescriptionMedicineResponse;
import com.hospital.hospital_queue_management.dto.response.PrescriptionResponse;
import com.hospital.hospital_queue_management.entity.*;
import com.hospital.hospital_queue_management.enums.QueueStatus;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.DoctorRepository;
import com.hospital.hospital_queue_management.repository.PatientRepository;
import com.hospital.hospital_queue_management.repository.PrescriptionRepository;
import com.hospital.hospital_queue_management.repository.QueueEntryRepository;
import com.hospital.hospital_queue_management.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional
    public PrescriptionResponse createPrescription(
            Long doctorUserId,
            Long queueEntryId,
            PrescriptionRequest request
    ) {

        // 1. Get doctor profile
        Doctor doctor = doctorRepository.findByUserId(doctorUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));

        // 2. Get queue entry
        QueueEntry queueEntry = queueEntryRepository.findById(queueEntryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Queue entry not found"));

        // 3. Verify queue entry belongs to this doctor
        if (!queueEntry.getDoctor().getId().equals(doctor.getId())) {
            throw new IllegalArgumentException(
                    "Queue entry does not belong to this doctor"
            );
        }

        // 4. Prescription only after completed consultation
        if (queueEntry.getStatus() != QueueStatus.IN_CONSULTATION) {
            throw new IllegalArgumentException(
                    "Prescription can only be created during an ongoing consultation"
            );
        }

        // 5. Prevent duplicate prescription
        if (prescriptionRepository.existsByQueueEntryId(queueEntryId)) {
            throw new ResourceAlreadyExistsException(
                    "Prescription already exists for this consultation"
            );
        }

        // 6. Validate duplicate medicines
        Set<String> medicineNames = new HashSet<>();

        for (PrescriptionMedicineRequest medicineRequest : request.getMedicines()) {

            String normalizedName = medicineRequest.getMedicineName()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            if (!medicineNames.add(normalizedName)) {
                throw new ResourceAlreadyExistsException(
                        "Duplicate medicine in prescription: "
                                + medicineRequest.getMedicineName()
                );
            }
        }

        // 7. Get patient from queue entry
        Patient patient = queueEntry.getPatient();

        // 8. Create prescription
        Prescription prescription = Prescription.builder()
                .queueEntry(queueEntry)
                .patient(patient)
                .doctor(doctor)
                .diagnosis(request.getDiagnosis())
                .notes(request.getNotes())
                .build();

        // 9. Create medicines
        for (PrescriptionMedicineRequest medicineRequest : request.getMedicines()) {

            PrescriptionMedicine medicine = PrescriptionMedicine.builder()
                    .prescription(prescription)
                    .medicineName(medicineRequest.getMedicineName().trim())
                    .dosage(medicineRequest.getDosage().trim())
                    .quantity(medicineRequest.getQuantity())
                    .timings(new HashSet<>(medicineRequest.getTimings()))
                    .duration(medicineRequest.getDuration())
                    .instructions(medicineRequest.getInstructions())
                    .build();

            prescription.getMedicines().add(medicine);
        }

        // 10. Save prescription + medicines through CascadeType.ALL
        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        // 11. Convert to response
        return mapToResponse(savedPrescription);
    }

    @Override
    @Transactional
    public Page<PrescriptionResponse> getPatientPrescriptions(
            Long patientUserId,
            Pageable pageable
    ) {

        Patient patient = patientRepository.findByUserId(patientUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient profile not found"));

        Page<Prescription> prescriptions =
                prescriptionRepository.findByPatientId(
                        patient.getId(),
                        pageable
                );

        return prescriptions.map(this::mapToResponse);
    }

    private PrescriptionResponse mapToResponse(
            Prescription prescription
    ) {

        String doctorName =
                prescription.getDoctor().getUser().getName();

        List<PrescriptionMedicineResponse> medicines =
                prescription.getMedicines()
                        .stream()
                        .map(medicine ->
                                PrescriptionMedicineResponse.builder()
                                        .id(medicine.getId())
                                        .medicineName(medicine.getMedicineName())
                                        .dosage(medicine.getDosage())
                                        .quantity(medicine.getQuantity())
                                        .timings(medicine.getTimings())
                                        .duration(medicine.getDuration())
                                        .instructions(medicine.getInstructions())
                                        .build()
                        )
                        .toList();

        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .patientId(prescription.getPatient().getId())
                .doctorName(doctorName.trim())
                .diagnosis(prescription.getDiagnosis())
                .notes(prescription.getNotes())
                .medicines(medicines)
                .createdAt(prescription.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyPrescriptionResponse getPrescriptionForPharmacy(
            Long prescriptionId
    ) {

        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription not found with id: " + prescriptionId
                        ));

        return mapToPharmacyResponse(prescription);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PharmacyPrescriptionResponse> getPrescriptionsForPharmacy(
            Pageable pageable
    ) {

        return prescriptionRepository
                .findAll(pageable)
                .map(this::mapToPharmacyResponse);
    }

    private PharmacyPrescriptionResponse mapToPharmacyResponse(
            Prescription prescription
    ) {

        String doctorName =
                prescription.getDoctor().getUser().getName();

        List<PharmacyPrescriptionMedicineResponse> medicines =
                prescription.getMedicines()
                        .stream()
                        .map(medicine ->
                                PharmacyPrescriptionMedicineResponse.builder()
                                        .id(medicine.getId())
                                        .medicineName(medicine.getMedicineName())
                                        .dosage(medicine.getDosage())
                                        .quantity(medicine.getQuantity())
                                        .timings(medicine.getTimings())
                                        .duration(medicine.getDuration())
                                        .instructions(medicine.getInstructions())
                                        .build()
                        )
                        .toList();

        return PharmacyPrescriptionResponse.builder()
                .prescriptionId(prescription.getId())
                .patientId(prescription.getPatient().getId())
                .patientName(prescription.getPatient().getUser().getName())
                .doctorId(prescription.getDoctor().getId())
                .doctorName(doctorName.trim())
                .diagnosis(prescription.getDiagnosis())
                .notes(prescription.getNotes())
                .medicines(medicines)
                .createdAt(prescription.getCreatedAt())
                .build();
    }
}