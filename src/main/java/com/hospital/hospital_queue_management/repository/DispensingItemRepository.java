package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.DispensingItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DispensingItemRepository
        extends JpaRepository<DispensingItem, Long> {
}