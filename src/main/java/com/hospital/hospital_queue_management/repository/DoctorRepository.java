package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.Doctor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @EntityGraph(attributePaths = "user")
    Optional<Doctor> findByUserId(Long userId);

    @EntityGraph(attributePaths = "user")
    Optional<Doctor> findById(Long id);

    boolean existsByUserId(Long userId);

    boolean existsByLicenseNumber(String licenseNumber);

    @EntityGraph(attributePaths = "user")
    List<Doctor> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "user")
    List<Doctor> findAllByUserEnabledTrueOrderByCreatedAtDesc();

    long countByUserEnabledTrue();
}
