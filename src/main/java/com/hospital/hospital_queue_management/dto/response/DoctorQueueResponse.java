package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.QueueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQueueResponse {

    private Long queueId;
    private Long doctorId;
    private LocalDate queueDate;
    private Integer totalPatients;
}