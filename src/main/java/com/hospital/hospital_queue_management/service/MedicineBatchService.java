package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.MedicineBatchRequest;
import com.hospital.hospital_queue_management.dto.response.MedicineBatchResponse;

import java.util.List;

public interface MedicineBatchService {

    MedicineBatchResponse addBatch(MedicineBatchRequest request);

    List<MedicineBatchResponse> getAllBatches();

    MedicineBatchResponse getBatchById(Long id);

    List<MedicineBatchResponse> getBatchesByMedicine(Long medicineId);
}