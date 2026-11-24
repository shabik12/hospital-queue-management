package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.AppointmentRequest;
import com.hospital.hospital_queue_management.dto.response.AppointmentResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients/appointments")
@RequiredArgsConstructor
public class PatientAppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            Authentication authentication,
            @Valid @RequestBody AppointmentRequest request) {

        User user = (User) authentication.getPrincipal();

        AppointmentResponse appointmentResponse =
                appointmentService.bookAppointment(
                        user.getId(),
                        request
                );

        ApiResponse<AppointmentResponse> response =
                ApiResponse.<AppointmentResponse>builder()
                        .success(true)
                        .message("Appointment booked successfully")
                        .data(appointmentResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>>
    getMyAppointments(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<AppointmentResponse> appointments =
                appointmentService.getMyAppointments(user.getId());

        ApiResponse<List<AppointmentResponse>> response =
                ApiResponse.<List<AppointmentResponse>>builder()
                        .success(true)
                        .message("Appointments retrieved successfully")
                        .data(appointments)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<ApiResponse<AppointmentResponse>>
    getMyAppointment(
            Authentication authentication,
            @PathVariable Long appointmentId) {

        User user = (User) authentication.getPrincipal();

        AppointmentResponse appointment =
                appointmentService.getMyAppointment(
                        user.getId(),
                        appointmentId
                );

        ApiResponse<AppointmentResponse> response =
                ApiResponse.<AppointmentResponse>builder()
                        .success(true)
                        .message("Appointment retrieved successfully")
                        .data(appointment)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            Authentication authentication,
            @PathVariable Long appointmentId) {

        User user = (User) authentication.getPrincipal();

        appointmentService.cancelAppointment(
                user.getId(),
                appointmentId
        );

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Appointment cancelled successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}