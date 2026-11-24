package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.response.DispensingItemResponse;
import com.hospital.hospital_queue_management.dto.response.DispensingResponse;
import com.hospital.hospital_queue_management.entity.Dispensing;
import com.hospital.hospital_queue_management.entity.DispensingItem;
import com.hospital.hospital_queue_management.entity.Medicine;
import com.hospital.hospital_queue_management.entity.MedicineBatch;
import com.hospital.hospital_queue_management.entity.Prescription;
import com.hospital.hospital_queue_management.entity.PrescriptionMedicine;
import com.hospital.hospital_queue_management.enums.PrescriptionStatus;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.DispensingRepository;
import com.hospital.hospital_queue_management.repository.MedicineBatchRepository;
import com.hospital.hospital_queue_management.repository.MedicineRepository;
import com.hospital.hospital_queue_management.repository.PrescriptionRepository;
import com.hospital.hospital_queue_management.service.DispensingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispensingServiceImpl implements DispensingService {

    private final PrescriptionRepository prescriptionRepository;
    private final DispensingRepository dispensingRepository;
    private final MedicineRepository medicineRepository;
    private final MedicineBatchRepository medicineBatchRepository;

    @Override
    @Transactional
    public DispensingResponse dispensePrescription(Long prescriptionId) {

        /*
         * STEP 1:
         * Find the prescription.
         */
        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Prescription not found with id: " + prescriptionId
                        ));

        /*
         * STEP 2:
         * A prescription can only be dispensed once.
         */
        if (prescription.getStatus() == PrescriptionStatus.DISPENSED) {
            throw new ResourceAlreadyExistsException(
                    "Prescription has already been dispensed"
            );
        }

        /*
         * STEP 3:
         * Extra protection against duplicate dispensing records.
         */
        if (dispensingRepository.existsByPrescriptionId(prescriptionId)) {
            throw new ResourceAlreadyExistsException(
                    "Dispensing already exists for prescription id: "
                            + prescriptionId
            );
        }

        /*
         * STEP 4:
         * Validate the complete prescription BEFORE
         * changing any stock.
         */
        for (PrescriptionMedicine prescriptionMedicine
                : prescription.getMedicines()) {

            Medicine medicine = medicineRepository
                    .findByNameIgnoreCase(
                            prescriptionMedicine.getMedicineName().trim()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Medicine not found: "
                                            + prescriptionMedicine.getMedicineName()
                            ));

            if (!Boolean.TRUE.equals(medicine.getActive())) {
                throw new IllegalArgumentException(
                        "Medicine is inactive: " + medicine.getName()
                );
            }

            int requiredQuantity =
                    prescriptionMedicine.getQuantity();

            int availableQuantity =
                    getTotalAvailableQuantity(medicine.getId());

            if (availableQuantity < requiredQuantity) {
                throw new IllegalArgumentException(
                        "Insufficient stock for medicine: "
                                + medicine.getName()
                                + ". Required: "
                                + requiredQuantity
                                + ", Available: "
                                + availableQuantity
                );
            }
        }

        /*
         * STEP 5:
         * All medicines have sufficient stock.
         * Now create the dispensing transaction.
         */
        Dispensing dispensing = Dispensing.builder()
                .prescription(prescription)
                .build();

        List<DispensingItem> dispensingItems = new ArrayList<>();

        /*
         * STEP 6:
         * Deduct stock using FEFO.
         */
        for (PrescriptionMedicine prescriptionMedicine
                : prescription.getMedicines()) {

            Medicine medicine = medicineRepository
                    .findByNameIgnoreCase(
                            prescriptionMedicine.getMedicineName().trim()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Medicine not found: "
                                            + prescriptionMedicine.getMedicineName()
                            ));

            int remainingQuantity =
                    prescriptionMedicine.getQuantity();

            List<MedicineBatch> batches =
                    medicineBatchRepository
                            .findByMedicineIdOrderByExpiryDateAsc(
                                    medicine.getId()
                            );

            for (MedicineBatch batch : batches) {

                if (remainingQuantity <= 0) {
                    break;
                }

                if (batch.getAvailableQuantity() <= 0) {
                    continue;
                }

                /*
                 * Do not use expired batches.
                 */
                if (batch.getExpiryDate().isBefore(LocalDate.now())) {
                    continue;
                }

                int quantityToDispense =
                        Math.min(
                                remainingQuantity,
                                batch.getAvailableQuantity()
                        );

                batch.setAvailableQuantity(
                        batch.getAvailableQuantity()
                                - quantityToDispense
                );

                remainingQuantity -= quantityToDispense;

                DispensingItem dispensingItem =
                        DispensingItem.builder()
                                .dispensing(dispensing)
                                .medicine(medicine)
                                .medicineBatch(batch)
                                .quantity(quantityToDispense)
                                .build();

                dispensingItems.add(dispensingItem);
            }

            /*
             * This should normally never happen because
             * stock was validated above.
             */
            if (remainingQuantity > 0) {
                throw new IllegalArgumentException(
                        "Unable to dispense complete quantity for medicine: "
                                + medicine.getName()
                );
            }
        }

        /*
         * STEP 7:
         * Attach items to dispensing.
         */
        dispensing.setItems(dispensingItems);

        /*
         * STEP 8:
         * Mark prescription as DISPENSED.
         */
        prescription.setStatus(PrescriptionStatus.DISPENSED);

        /*
         * STEP 9:
         * Save dispensing.
         * CascadeType.ALL saves the DispensingItems.
         */
        Dispensing savedDispensing =
                dispensingRepository.save(dispensing);

        /*
         * STEP 10:
         * Map entity to response.
         */
        return mapToResponse(savedDispensing);
    }

    private int getTotalAvailableQuantity(Long medicineId) {

        List<MedicineBatch> batches =
                medicineBatchRepository
                        .findByMedicineIdOrderByExpiryDateAsc(
                                medicineId
                        );

        return batches.stream()
                .filter(batch ->
                        batch.getAvailableQuantity() != null
                                && batch.getAvailableQuantity() > 0
                )
                .filter(batch ->
                        batch.getExpiryDate() != null
                                && !batch.getExpiryDate()
                                .isBefore(LocalDate.now())
                )
                .mapToInt(MedicineBatch::getAvailableQuantity)
                .sum();
    }

    private DispensingResponse mapToResponse(
            Dispensing dispensing
    ) {

        List<DispensingItemResponse> items =
                dispensing.getItems()
                        .stream()
                        .map(item ->
                                DispensingItemResponse.builder()
                                        .medicineId(
                                                item.getMedicine().getId()
                                        )
                                        .medicineName(
                                                item.getMedicine().getName()
                                        )
                                        .batchId(
                                                item.getMedicineBatch().getId()
                                        )
                                        .batchNumber(
                                                item.getMedicineBatch()
                                                        .getBatchNumber()
                                        )
                                        .quantity(
                                                item.getQuantity()
                                        )
                                        .build()
                        )
                        .toList();

        return DispensingResponse.builder()
                .dispensingId(dispensing.getId())
                .prescriptionId(
                        dispensing.getPrescription().getId()
                )
                .dispensedAt(dispensing.getDispensedAt())
                .items(items)
                .build();
    }
}