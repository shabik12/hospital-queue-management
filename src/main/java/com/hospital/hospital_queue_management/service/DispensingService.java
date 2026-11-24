package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.response.DispensingResponse;

public interface DispensingService {

    DispensingResponse dispensePrescription(Long prescriptionId);
}