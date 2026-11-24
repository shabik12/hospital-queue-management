package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.response.AppointmentResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/appointments")
@RequiredArgsConstructor
public class DoctorAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>>
    getMyAppointments(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<AppointmentResponse> appointments =
                appointmentService.getDoctorAppointments(
                        user.getId()
                );

        ApiResponse<List<AppointmentResponse>> response =
                ApiResponse.<List<AppointmentResponse>>builder()
                        .success(true)
                        .message("Doctor appointments retrieved successfully")
                        .data(appointments)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}