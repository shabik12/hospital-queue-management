package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.MedicineBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineBatchRepository extends JpaRepository<MedicineBatch, Long> {

    boolean existsByBatchNumberIgnoreCase(String batchNumber);

    List<MedicineBatch> findByMedicineIdOrderByExpiryDateAsc(Long medicineId);
}