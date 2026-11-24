package com.hospital.hospital_queue_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminDashboardResponse {

    private Long todaysAppointments;
    private Long todaysCompleted;
    private Long todaysCancelled;
    private Long currentWaitingPatients;
    private Long activeDoctors;
    private Long prescriptionsIssuedToday;
    private Long pendingPharmacyPrescriptions;
    private Long dispensedPrescriptionsToday;
}
