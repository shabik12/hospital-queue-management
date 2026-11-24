package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.response.QueueEntryResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/queue")
@RequiredArgsConstructor
public class DoctorQueueController {

    private final QueueService queueService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<QueueEntryResponse>>> getTodayQueue(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<QueueEntryResponse> queue =
                queueService.getTodayQueue(user.getId());

        ApiResponse<List<QueueEntryResponse>> response =
                ApiResponse.<List<QueueEntryResponse>>builder()
                        .success(true)
                        .message("Today's queue retrieved successfully")
                        .data(queue)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{queueEntryId}/call")
    public ResponseEntity<ApiResponse<QueueEntryResponse>> callPatient(
            Authentication authentication,
            @PathVariable Long queueEntryId) {

        User user = (User) authentication.getPrincipal();

        QueueEntryResponse queueEntry =
                queueService.callPatient(
                        user.getId(),
                        queueEntryId
                );

        ApiResponse<QueueEntryResponse> response =
                ApiResponse.<QueueEntryResponse>builder()
                        .success(true)
                        .message("Patient called successfully")
                        .data(queueEntry)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{queueEntryId}/start")
    public ResponseEntity<ApiResponse<QueueEntryResponse>> startConsultation(
            Authentication authentication,
            @PathVariable Long queueEntryId) {

        User user = (User) authentication.getPrincipal();

        QueueEntryResponse queueEntry =
                queueService.startConsultation(
                        user.getId(),
                        queueEntryId
                );

        ApiResponse<QueueEntryResponse> response =
                ApiResponse.<QueueEntryResponse>builder()
                        .success(true)
                        .message("Consultation started successfully")
                        .data(queueEntry)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{queueEntryId}/complete")
    public ResponseEntity<ApiResponse<QueueEntryResponse>> completeConsultation(
            Authentication authentication,
            @PathVariable Long queueEntryId) {

        User user = (User) authentication.getPrincipal();

        QueueEntryResponse queueEntry =
                queueService.completeConsultation(
                        user.getId(),
                        queueEntryId
                );

        ApiResponse<QueueEntryResponse> response =
                ApiResponse.<QueueEntryResponse>builder()
                        .success(true)
                        .message("Consultation completed successfully")
                        .data(queueEntry)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}