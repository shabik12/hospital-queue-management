package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.Prescription;
import com.hospital.hospital_queue_management.enums.PrescriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    boolean existsByQueueEntryId(Long queueEntryId);

    Page<Prescription> findByPatientId(
            Long patientId,
            Pageable pageable
    );

    Page<Prescription> findAll(Pageable pageable);

    long countByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    long countByCreatedAtBetweenAndStatus(
            LocalDateTime start,
            LocalDateTime end,
            PrescriptionStatus status
    );

    long countByStatus(PrescriptionStatus status);
}
