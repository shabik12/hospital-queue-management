package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.response.ReceptionAppointmentResponse;
import com.hospital.hospital_queue_management.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reception/appointments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECEPTION')")
public class ReceptionAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<ReceptionAppointmentResponse>>> getTodayAppointments() {

        List<ReceptionAppointmentResponse> appointments =
                appointmentService.getTodayAppointments();

        ApiResponse<List<ReceptionAppointmentResponse>> response =
                ApiResponse.<List<ReceptionAppointmentResponse>>builder()
                        .success(true)
                        .message("Today's appointments retrieved successfully")
                        .data(appointments)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}