package com.hospital.hospital_queue_management.mapper;

import com.hospital.hospital_queue_management.dto.request.MedicineBatchRequest;
import com.hospital.hospital_queue_management.dto.response.MedicineBatchResponse;
import com.hospital.hospital_queue_management.entity.Medicine;
import com.hospital.hospital_queue_management.entity.MedicineBatch;
import org.springframework.stereotype.Component;

@Component
public class MedicineBatchMapper {

    public MedicineBatch toEntity(
            MedicineBatchRequest request,
            Medicine medicine) {

        return MedicineBatch.builder()
                .medicine(medicine)
                .batchNumber(request.getBatchNumber().trim())
                .expiryDate(request.getExpiryDate())
                .purchaseDate(request.getPurchaseDate())
                .quantity(request.getQuantity())
                .availableQuantity(request.getQuantity())
                .build();
    }

    public MedicineBatchResponse toResponse(MedicineBatch batch) {

        return MedicineBatchResponse.builder()
                .id(batch.getId())
                .medicineId(batch.getMedicine().getId())
                .medicineName(batch.getMedicine().getName())
                .batchNumber(batch.getBatchNumber())
                .expiryDate(batch.getExpiryDate())
                .purchaseDate(batch.getPurchaseDate())
                .quantity(batch.getQuantity())
                .availableQuantity(batch.getAvailableQuantity())
                .createdAt(batch.getCreatedAt())
                .updatedAt(batch.getUpdatedAt())
                .build();
    }
}