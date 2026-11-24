package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.MedicineRequest;
import com.hospital.hospital_queue_management.dto.request.MedicineUpdateRequest;
import com.hospital.hospital_queue_management.dto.response.MedicineResponse;

import java.util.List;

public interface MedicineService {

    MedicineResponse createMedicine(MedicineRequest request);

    List<MedicineResponse> getAllMedicines();

    MedicineResponse getMedicineById(Long id);

    MedicineResponse updateMedicine(
            Long id,
            MedicineUpdateRequest request
    );

    MedicineResponse activateMedicine(Long id);

    MedicineResponse deactivateMedicine(Long id);
}