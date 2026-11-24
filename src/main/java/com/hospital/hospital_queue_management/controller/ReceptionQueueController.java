package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.response.QueueEntryResponse;
import com.hospital.hospital_queue_management.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reception/queue")
@RequiredArgsConstructor
public class ReceptionQueueController {

    private final QueueService queueService;

    @PostMapping("/check-in/{appointmentId}")
    public ResponseEntity<ApiResponse<QueueEntryResponse>> checkInPatient(
            @PathVariable Long appointmentId) {

        QueueEntryResponse queueEntry =
                queueService.checkIn(appointmentId);

        ApiResponse<QueueEntryResponse> response =
                ApiResponse.<QueueEntryResponse>builder()
                        .success(true)
                        .message("Patient checked in successfully")
                        .data(queueEntry)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}