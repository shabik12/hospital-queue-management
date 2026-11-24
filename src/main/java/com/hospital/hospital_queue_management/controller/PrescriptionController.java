package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.PrescriptionRequest;
import com.hospital.hospital_queue_management.dto.response.PrescriptionResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/doctors/queue/{queueEntryId}/prescription")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(
            @PathVariable Long queueEntryId,
            @Valid @RequestBody PrescriptionRequest request,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        PrescriptionResponse response =
                prescriptionService.createPrescription(
                        user.getId(),
                        queueEntryId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<PrescriptionResponse>builder()
                                .success(true)
                                .message("Prescription created successfully")
                                .data(response)
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

    @GetMapping("/patients/prescriptions")
    public ResponseEntity<ApiResponse<Page<PrescriptionResponse>>> getPatientPrescriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<PrescriptionResponse> response =
                prescriptionService.getPatientPrescriptions(
                        user.getId(),
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.<Page<PrescriptionResponse>>builder()
                        .success(true)
                        .message("Prescriptions retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}