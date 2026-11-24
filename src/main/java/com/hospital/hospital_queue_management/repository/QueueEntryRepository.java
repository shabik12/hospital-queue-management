package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.QueueEntry;
import com.hospital.hospital_queue_management.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    boolean existsByAppointmentId(Long appointmentId);

    Optional<QueueEntry> findByAppointmentId(Long appointmentId);

    @Query("""
            SELECT COALESCE(MAX(q.queueNumber), 0)
            FROM QueueEntry q
            WHERE q.doctor.id = :doctorId
              AND q.queueDate = :queueDate
            """)
    Optional<Integer> findMaxQueueNumberByDoctorIdAndQueueDate(
            @Param("doctorId") Long doctorId,
            @Param("queueDate") LocalDate queueDate
    );

    List<QueueEntry> findByDoctorIdAndQueueDateOrderByQueueNumberAsc(
            Long doctorId,
            LocalDate queueDate
    );

    List<QueueEntry> findByDoctorIdAndQueueDateAndStatusOrderByQueueNumberAsc(
            Long doctorId,
            LocalDate queueDate,
            QueueStatus status
    );

    long countByQueueDateAndStatus(
            LocalDate queueDate,
            QueueStatus status
    );

    long countByDoctorIdAndQueueDateAndStatus(
            Long doctorId,
            LocalDate queueDate,
            QueueStatus status
    );

    Optional<QueueEntry> findFirstByDoctorIdAndQueueDateAndStatusOrderByQueueNumberAsc(
            Long doctorId,
            LocalDate queueDate,
            QueueStatus status
    );

    Optional<QueueEntry> findByIdAndDoctorUserId(
            Long queueEntryId,
            Long userId
    );
}
