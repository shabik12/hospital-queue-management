package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.DoctorSchedule;
import com.hospital.hospital_queue_management.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorIdAndActiveTrue(Long doctorId);

    List<DoctorSchedule> findByDoctorIdOrderByDayOfWeekAscStartTimeAsc(
            Long doctorId
    );

    boolean existsByDoctorIdAndDayOfWeek(
            Long doctorId,
            DayOfWeek dayOfWeek
    );

    boolean existsByDoctorIdAndDayOfWeekAndIdNot(
            Long doctorId,
            DayOfWeek dayOfWeek,
            Long id
    );

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeekAndActiveTrue(
            Long doctorId,
            DayOfWeek dayOfWeek
    );

    Optional<DoctorSchedule> findByIdAndDoctorId(Long id, Long doctorId);
}
