package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Medicine> findByNameIgnoreCase(String name);
}