package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.QueueStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueEntryResponse {

    private Long id;



    private Long appointmentId;

    private Long patientId;

    private Integer queueNumber;

    private QueueStatus status;

    private LocalDateTime checkedInAt;

    private LocalDateTime calledAt;

    private LocalDateTime consultationStartedAt;

    private LocalDateTime completedAt;
}
