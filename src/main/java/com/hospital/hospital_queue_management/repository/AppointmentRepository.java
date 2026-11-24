package com.hospital.hospital_queue_management.repository;

import com.hospital.hospital_queue_management.entity.Appointment;
import com.hospital.hospital_queue_management.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientIdOrderByAppointmentDateAscStartTimeAsc(
            Long patientId
    );

    List<Appointment> findByDoctorIdOrderByAppointmentDateAscStartTimeAsc(
            Long doctorId
    );

    Optional<Appointment> findByIdAndPatientId(
            Long appointmentId,
            Long patientId
    );

    boolean existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNot(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime startTime,
            AppointmentStatus status
    );

    long countByAppointmentDate(LocalDate appointmentDate);

    long countByAppointmentDateAndStatus(
            LocalDate appointmentDate,
            AppointmentStatus status
    );

    long countByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );

    long countByDoctorIdAndAppointmentDateAndStatus(
            Long doctorId,
            LocalDate appointmentDate,
            AppointmentStatus status
    );
    List<Appointment> findByAppointmentDateOrderByStartTimeAsc(LocalDate appointmentDate);
}
