package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.DoctorRequest;
import com.hospital.hospital_queue_management.dto.response.DoctorResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<DoctorResponse>> createProfile(
            Authentication authentication,
            @Valid @RequestBody DoctorRequest request) {

        User user = (User) authentication.getPrincipal();

        DoctorResponse doctorResponse =
                doctorService.createProfile(
                        user.getId(),
                        request
                );

        ApiResponse<DoctorResponse> response =
                ApiResponse.<DoctorResponse>builder()
                        .success(true)
                        .message("Doctor profile created successfully")
                        .data(doctorResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<DoctorResponse>> getMyProfile(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        DoctorResponse doctorResponse =
                doctorService.getMyProfile(user.getId());

        ApiResponse<DoctorResponse> response =
                ApiResponse.<DoctorResponse>builder()
                        .success(true)
                        .message("Doctor profile retrieved successfully")
                        .data(doctorResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<java.util.List<DoctorResponse>>> getAllDoctors() {
        java.util.List<DoctorResponse> doctors = doctorService.getAllDoctors();
        ApiResponse<java.util.List<DoctorResponse>> response =
                ApiResponse.<java.util.List<DoctorResponse>>builder()
                        .success(true)
                        .message("Doctors retrieved successfully")
                        .data(doctors)
                        .timestamp(LocalDateTime.now())
                        .build();
        return ResponseEntity.ok(response);
    }
}