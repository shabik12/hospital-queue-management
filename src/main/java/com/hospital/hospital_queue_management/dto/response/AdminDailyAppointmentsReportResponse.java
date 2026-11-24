package com.hospital.hospital_queue_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class AdminDailyAppointmentsReportResponse {

    private LocalDate reportDate;
    private Long totalAppointments;
    private Long confirmedAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
}
