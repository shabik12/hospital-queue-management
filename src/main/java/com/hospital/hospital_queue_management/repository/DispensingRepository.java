package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.Dispensing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DispensingRepository extends JpaRepository<Dispensing, Long> {

    boolean existsByPrescriptionId(Long prescriptionId);
    long countByDispensedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}
