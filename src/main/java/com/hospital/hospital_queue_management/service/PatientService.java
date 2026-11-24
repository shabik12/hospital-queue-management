package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.PatientRequest;
import com.hospital.hospital_queue_management.dto.response.PatientResponse;

public interface PatientService {

    PatientResponse createProfile(
            Long userId,
            PatientRequest request
    );

    PatientResponse getMyProfile(Long userId);
}