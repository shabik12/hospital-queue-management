package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.AdminCreateUserRequest;
import com.hospital.hospital_queue_management.dto.request.AdminDoctorCreateRequest;
import com.hospital.hospital_queue_management.dto.request.AdminDoctorUpdateRequest;
import com.hospital.hospital_queue_management.dto.request.DoctorScheduleRequest;
import com.hospital.hospital_queue_management.dto.response.AdminCreateUserResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDailyAppointmentsReportResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDashboardResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDoctorResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDoctorWorkloadReportResponse;
import com.hospital.hospital_queue_management.dto.response.AdminPharmacyReportResponse;
import com.hospital.hospital_queue_management.dto.response.DoctorScheduleResponse;
import com.hospital.hospital_queue_management.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ============================================================
    // STAFF MANAGEMENT
    // ============================================================

    @PostMapping("/staff")
    public ResponseEntity<ApiResponse<AdminCreateUserResponse>> createStaffUser(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {

        AdminCreateUserResponse response =
                adminService.createStaffUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<AdminCreateUserResponse>builder()
                        .success(true)
                        .message("Staff account created successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    // ============================================================
    // DOCTOR MANAGEMENT
    // ============================================================

    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<AdminDoctorResponse>> createDoctor(
            @Valid @RequestBody AdminDoctorCreateRequest request
    ) {

        AdminDoctorResponse response =
                adminService.createDoctor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<AdminDoctorResponse>builder()
                        .success(true)
                        .message("Doctor created successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<AdminDoctorResponse>>> getDoctors() {

        List<AdminDoctorResponse> response =
                adminService.getDoctors();

        return ResponseEntity.ok(
                ApiResponse.<List<AdminDoctorResponse>>builder()
                        .success(true)
                        .message("Doctors retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<AdminDoctorResponse>> getDoctor(
            @PathVariable Long doctorId
    ) {

        AdminDoctorResponse response =
                adminService.getDoctor(doctorId);

        return ResponseEntity.ok(
                ApiResponse.<AdminDoctorResponse>builder()
                        .success(true)
                        .message("Doctor retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<ApiResponse<AdminDoctorResponse>> updateDoctor(
            @PathVariable Long doctorId,
            @Valid @RequestBody AdminDoctorUpdateRequest request
    ) {

        AdminDoctorResponse response =
                adminService.updateDoctor(doctorId, request);

        return ResponseEntity.ok(
                ApiResponse.<AdminDoctorResponse>builder()
                        .success(true)
                        .message("Doctor updated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/doctors/{doctorId}/activate")
    public ResponseEntity<ApiResponse<AdminDoctorResponse>> activateDoctor(
            @PathVariable Long doctorId
    ) {

        AdminDoctorResponse response =
                adminService.activateDoctor(doctorId);

        return ResponseEntity.ok(
                ApiResponse.<AdminDoctorResponse>builder()
                        .success(true)
                        .message("Doctor activated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/doctors/{doctorId}/deactivate")
    public ResponseEntity<ApiResponse<AdminDoctorResponse>> deactivateDoctor(
            @PathVariable Long doctorId
    ) {

        AdminDoctorResponse response =
                adminService.deactivateDoctor(doctorId);

        return ResponseEntity.ok(
                ApiResponse.<AdminDoctorResponse>builder()
                        .success(true)
                        .message("Doctor deactivated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ============================================================
    // DOCTOR SCHEDULE MANAGEMENT
    // ============================================================

    @PostMapping("/doctors/{doctorId}/schedules")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> createDoctorSchedule(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorScheduleRequest request
    ) {

        DoctorScheduleResponse response =
                adminService.createDoctorSchedule(doctorId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Doctor schedule created successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @GetMapping("/doctors/{doctorId}/schedules")
    public ResponseEntity<ApiResponse<List<DoctorScheduleResponse>>> getDoctorSchedules(
            @PathVariable Long doctorId
    ) {

        List<DoctorScheduleResponse> response =
                adminService.getDoctorSchedules(doctorId);

        return ResponseEntity.ok(
                ApiResponse.<List<DoctorScheduleResponse>>builder()
                        .success(true)
                        .message("Doctor schedules retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> updateDoctorSchedule(
            @PathVariable Long scheduleId,
            @Valid @RequestBody DoctorScheduleRequest request
    ) {

        DoctorScheduleResponse response =
                adminService.updateDoctorSchedule(scheduleId, request);

        return ResponseEntity.ok(
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Doctor schedule updated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/schedules/{scheduleId}/activate")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> activateDoctorSchedule(
            @PathVariable Long scheduleId
    ) {

        DoctorScheduleResponse response =
                adminService.activateDoctorSchedule(scheduleId);

        return ResponseEntity.ok(
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Doctor schedule activated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PatchMapping("/schedules/{scheduleId}/deactivate")
    public ResponseEntity<ApiResponse<DoctorScheduleResponse>> deactivateDoctorSchedule(
            @PathVariable Long scheduleId
    ) {

        DoctorScheduleResponse response =
                adminService.deactivateDoctorSchedule(scheduleId);

        return ResponseEntity.ok(
                ApiResponse.<DoctorScheduleResponse>builder()
                        .success(true)
                        .message("Doctor schedule deactivated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ============================================================
    // ADMIN DASHBOARD
    // ============================================================

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard(
            @RequestParam(required = false) LocalDate reportDate
    ) {

        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        AdminDashboardResponse response =
                adminService.getDashboard(reportDate);

        return ResponseEntity.ok(
                ApiResponse.<AdminDashboardResponse>builder()
                        .success(true)
                        .message("Admin dashboard retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // ============================================================
    // REPORTS
    // ============================================================

    @GetMapping("/reports/appointments")
    public ResponseEntity<ApiResponse<AdminDailyAppointmentsReportResponse>>
    getAppointmentsReport(
            @RequestParam(required = false) LocalDate reportDate
    ) {

        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        AdminDailyAppointmentsReportResponse response =
                adminService.getAppointmentsReport(reportDate);

        return ResponseEntity.ok(
                ApiResponse.<AdminDailyAppointmentsReportResponse>builder()
                        .success(true)
                        .message("Daily appointments report retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/reports/doctor-workload")
    public ResponseEntity<ApiResponse<AdminDoctorWorkloadReportResponse>>
    getDoctorWorkloadReport(
            @RequestParam(required = false) LocalDate reportDate
    ) {

        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        AdminDoctorWorkloadReportResponse response =
                adminService.getDoctorWorkloadReport(reportDate);

        return ResponseEntity.ok(
                ApiResponse.<AdminDoctorWorkloadReportResponse>builder()
                        .success(true)
                        .message("Doctor workload report retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/reports/pharmacy")
    public ResponseEntity<ApiResponse<AdminPharmacyReportResponse>>
    getPharmacyReport(
            @RequestParam(required = false) LocalDate reportDate
    ) {

        if (reportDate == null) {
            reportDate = LocalDate.now();
        }

        AdminPharmacyReportResponse response =
                adminService.getPharmacyReport(reportDate);

        return ResponseEntity.ok(
                ApiResponse.<AdminPharmacyReportResponse>builder()
                        .success(true)
                        .message("Pharmacy report retrieved successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}