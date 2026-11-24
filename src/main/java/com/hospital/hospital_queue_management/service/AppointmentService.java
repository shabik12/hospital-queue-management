package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.AppointmentRequest;
import com.hospital.hospital_queue_management.dto.response.AppointmentResponse;
import com.hospital.hospital_queue_management.dto.response.ReceptionAppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(
            Long userId,
            AppointmentRequest request
    );

    List<AppointmentResponse> getMyAppointments(
            Long userId
    );

    AppointmentResponse getMyAppointment(
            Long userId,
            Long appointmentId
    );
    List<ReceptionAppointmentResponse> getTodayAppointments();

    void cancelAppointment(
            Long userId,
            Long appointmentId
    );

    List<AppointmentResponse> getDoctorAppointments(
            Long userId
    );
}