
package com.hospital.hospital_queue_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminDoctorWorkloadReportResponse {

    private LocalDate reportDate;
    private List<DoctorWorkload> doctors;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DoctorWorkload {

        private Long doctorId;
        private String doctorName;
        private String specialization;
        private Long totalAppointments;
        private Long completedAppointments;
        private Long cancelledAppointments;
        private Long waitingPatients;
    }
}
