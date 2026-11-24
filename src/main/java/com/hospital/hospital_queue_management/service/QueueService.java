package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.response.QueueEntryResponse;

import java.util.List;

public interface QueueService {

    QueueEntryResponse checkIn(Long appointmentId);

    List<QueueEntryResponse> getTodayQueue(Long userId);

    QueueEntryResponse callPatient(
            Long userId,
            Long queueEntryId
    );

    QueueEntryResponse startConsultation(
            Long userId,
            Long queueEntryId
    );

    QueueEntryResponse completeConsultation(
            Long userId,
            Long queueEntryId
    );
}