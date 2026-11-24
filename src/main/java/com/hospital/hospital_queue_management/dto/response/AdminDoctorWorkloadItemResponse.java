package com.hospital.hospital_queue_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminDoctorWorkloadItemResponse {

    private Long doctorId;
    private String doctorName;
    private String specialization;
    private Long appointmentsToday;
    private Long completedConsultationsToday;
    private Long waitingPatientsToday;
    private Long cancelledAppointmentsToday;
}
