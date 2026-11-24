package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.PrescriptionRequest;
import com.hospital.hospital_queue_management.dto.response.PharmacyPrescriptionResponse;
import com.hospital.hospital_queue_management.dto.response.PrescriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionService {

    PrescriptionResponse createPrescription(
            Long doctorUserId,
            Long queueEntryId,
            PrescriptionRequest request
    );

    Page<PrescriptionResponse> getPatientPrescriptions(
            Long patientUserId,
            Pageable pageable
    );
    PharmacyPrescriptionResponse getPrescriptionForPharmacy(Long prescriptionId);

    Page<PharmacyPrescriptionResponse> getPrescriptionsForPharmacy(Pageable pageable);
}