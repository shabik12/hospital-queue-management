package com.hospital.hospital_queue_management.service;

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

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    AdminCreateUserResponse createStaffUser(
            AdminCreateUserRequest request
    );

    AdminDoctorResponse createDoctor(
            AdminDoctorCreateRequest request
    );

    List<AdminDoctorResponse> getDoctors();

    AdminDoctorResponse getDoctor(
            Long doctorId
    );

    AdminDoctorResponse updateDoctor(
            Long doctorId,
            AdminDoctorUpdateRequest request
    );

    AdminDoctorResponse activateDoctor(
            Long doctorId
    );

    AdminDoctorResponse deactivateDoctor(
            Long doctorId
    );

    DoctorScheduleResponse createDoctorSchedule(
            Long doctorId,
            DoctorScheduleRequest request
    );

    List<DoctorScheduleResponse> getDoctorSchedules(
            Long doctorId
    );

    DoctorScheduleResponse updateDoctorSchedule(
            Long scheduleId,
            DoctorScheduleRequest request
    );

    DoctorScheduleResponse activateDoctorSchedule(
            Long scheduleId
    );

    DoctorScheduleResponse deactivateDoctorSchedule(
            Long scheduleId
    );

    AdminDashboardResponse getDashboard(
            LocalDate reportDate
    );

    AdminDailyAppointmentsReportResponse getAppointmentsReport(
            LocalDate reportDate
    );

    AdminDoctorWorkloadReportResponse getDoctorWorkloadReport(
            LocalDate reportDate
    );

    AdminPharmacyReportResponse getPharmacyReport(
            LocalDate reportDate
    );
}