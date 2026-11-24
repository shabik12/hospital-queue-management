package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.DoctorScheduleRequest;
import com.hospital.hospital_queue_management.dto.response.DoctorScheduleResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService doctorScheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>>
    createSchedule(
            Authentication authentication,
            @Valid @RequestBody DoctorScheduleRequest request) {

        User user = (User) authentication.getPrincipal();

        DoctorScheduleResponse scheduleResponse =
                doctorScheduleService.createSchedule(
                        user.getId(),
                        request
                );

        ApiResponse<DoctorScheduleResponse> response =
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Doctor schedule created successfully")
                        .data(scheduleResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponse>>>
    getMySchedules(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<DoctorScheduleResponse> schedules =
                doctorScheduleService.getMySchedules(
                        user.getId()
                );

        ApiResponse<List<DoctorScheduleResponse>> response =
                ApiResponse.<List<DoctorScheduleResponse>>builder()
                        .success(true)
                        .message("Doctor schedules retrieved successfully")
                        .data(schedules)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponse>>>
    getDoctorSchedules(@PathVariable Long doctorId) {

        List<DoctorScheduleResponse> schedules =
                doctorScheduleService.getDoctorSchedules(doctorId);

        ApiResponse<List<DoctorScheduleResponse>> response =
                ApiResponse.<List<DoctorScheduleResponse>>builder()
                        .success(true)
                        .message("Doctor schedules retrieved successfully")
                        .data(schedules)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>>
    updateSchedule(
            Authentication authentication,
            @PathVariable Long scheduleId,
            @Valid @RequestBody DoctorScheduleRequest request) {

        User user = (User) authentication.getPrincipal();

        DoctorScheduleResponse scheduleResponse =
                doctorScheduleService.updateSchedule(
                        user.getId(),
                        scheduleId,
                        request
                );

        ApiResponse<DoctorScheduleResponse> response =
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Doctor schedule updated successfully")
                        .data(scheduleResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ApiResponse<Void>>
    deleteSchedule(
            Authentication authentication,
            @PathVariable Long scheduleId) {

        User user = (User) authentication.getPrincipal();

        doctorScheduleService.deleteSchedule(
                user.getId(),
                scheduleId
        );

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Doctor schedule deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}