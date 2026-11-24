package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.DoctorScheduleRequest;
import com.hospital.hospital_queue_management.dto.response.DoctorScheduleResponse;

import java.util.List;

public interface DoctorScheduleService {

    DoctorScheduleResponse createSchedule(
            Long userId,
            DoctorScheduleRequest request
    );

    List<DoctorScheduleResponse> getMySchedules(Long userId);

    DoctorScheduleResponse updateSchedule(
            Long userId,
            Long scheduleId,
            DoctorScheduleRequest request
    );

    void deleteSchedule(Long userId, Long scheduleId);

    List<DoctorScheduleResponse> getDoctorSchedules(Long doctorId);
}