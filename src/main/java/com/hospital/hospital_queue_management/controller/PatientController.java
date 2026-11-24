package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.PatientRequest;
import com.hospital.hospital_queue_management.dto.response.PatientResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/profile")
    public ResponseEntity<ApiResponse<PatientResponse>> createProfile(
            Authentication authentication,
            @Valid @RequestBody PatientRequest request) {

        User user = (User) authentication.getPrincipal();

        PatientResponse patientResponse =
                patientService.createProfile(
                        user.getId(),
                        request
                );

        ApiResponse<PatientResponse> response =
                ApiResponse.<PatientResponse>builder()
                        .success(true)
                        .message("Patient profile created successfully")
                        .data(patientResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<PatientResponse>> getMyProfile(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        PatientResponse patientResponse =
                patientService.getMyProfile(user.getId());

        ApiResponse<PatientResponse> response =
                ApiResponse.<PatientResponse>builder()
                        .success(true)
                        .message("Patient profile retrieved successfully")
                        .data(patientResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}