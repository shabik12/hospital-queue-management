package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.DoctorRequest;
import com.hospital.hospital_queue_management.dto.response.DoctorResponse;

import java.util.List;

public interface DoctorService {

    DoctorResponse createProfile(
            Long userId,
            DoctorRequest request
    );

    DoctorResponse getMyProfile(Long userId);

    List<DoctorResponse> getAllDoctors();
}