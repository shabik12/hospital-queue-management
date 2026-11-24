package com.hospital.hospital_queue_management.mapper;

import com.hospital.hospital_queue_management.dto.request.MedicineRequest;
import com.hospital.hospital_queue_management.dto.request.MedicineUpdateRequest;
import com.hospital.hospital_queue_management.dto.response.MedicineResponse;
import com.hospital.hospital_queue_management.entity.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public Medicine toEntity(MedicineRequest request) {

        return Medicine.builder()
                .name(request.getName().trim())
                .genericName(request.getGenericName().trim())
                .category(request.getCategory().trim())
                .unit(request.getUnit().trim())
                .active(true)
                .build();
    }

    public void updateEntity(
            Medicine medicine,
            MedicineUpdateRequest request
    ) {

        medicine.setName(request.getName().trim());
        medicine.setGenericName(request.getGenericName().trim());
        medicine.setCategory(request.getCategory().trim());
        medicine.setUnit(request.getUnit().trim());
    }

    public MedicineResponse toResponse(Medicine medicine) {

        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .genericName(medicine.getGenericName())
                .category(medicine.getCategory())
                .unit(medicine.getUnit())
                .active(medicine.getActive())
                .createdAt(medicine.getCreatedAt())
                .updatedAt(medicine.getUpdatedAt())
                .build();
    }
}