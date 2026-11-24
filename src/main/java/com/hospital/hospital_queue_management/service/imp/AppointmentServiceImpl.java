package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.AppointmentRequest;
import com.hospital.hospital_queue_management.dto.response.AppointmentResponse;
import com.hospital.hospital_queue_management.dto.response.ReceptionAppointmentResponse;
import com.hospital.hospital_queue_management.entity.Appointment;
import com.hospital.hospital_queue_management.entity.Doctor;
import com.hospital.hospital_queue_management.entity.DoctorSchedule;
import com.hospital.hospital_queue_management.entity.Patient;
import com.hospital.hospital_queue_management.enums.AppointmentStatus;
import com.hospital.hospital_queue_management.enums.DayOfWeek;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.mapper.AppointmentMapper;
import com.hospital.hospital_queue_management.repository.AppointmentRepository;
import com.hospital.hospital_queue_management.repository.DoctorRepository;
import com.hospital.hospital_queue_management.repository.DoctorScheduleRepository;
import com.hospital.hospital_queue_management.repository.PatientRepository;
import com.hospital.hospital_queue_management.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(
            Long userId,
            AppointmentRequest request) {

        // 1. Find patient profile
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        ));

        // 2. Find doctor
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found"
                        ));

        // 3. Convert Java date to our application DayOfWeek enum
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(
                request.getAppointmentDate()
                        .getDayOfWeek()
                        .name()
        );

        // 4. Find doctor's active schedule for that day
        DoctorSchedule schedule =
                doctorScheduleRepository
                        .findByDoctorIdAndDayOfWeekAndActiveTrue(
                                doctor.getId(),
                                dayOfWeek
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor is not available on this day"
                                ));

        // 5. Check requested time is within schedule
        LocalTime startTime = request.getStartTime();

        if (startTime.isBefore(schedule.getStartTime())
                || !startTime.isBefore(schedule.getEndTime())) {

            throw new IllegalArgumentException(
                    "Appointment time is outside doctor's schedule"
            );
        }

        // 6. Calculate appointment end time
        LocalTime endTime = startTime.plusMinutes(
                schedule.getSlotDurationMinutes()
        );

        // 7. Ensure appointment doesn't extend beyond schedule
        if (endTime.isAfter(schedule.getEndTime())) {

            throw new IllegalArgumentException(
                    "Appointment slot exceeds doctor's schedule"
            );
        }

        // 8. Ensure requested time aligns with slot duration
        long minutesFromScheduleStart =
                java.time.Duration.between(
                        schedule.getStartTime(),
                        startTime
                ).toMinutes();

        if (minutesFromScheduleStart
                % schedule.getSlotDurationMinutes() != 0) {

            throw new IllegalArgumentException(
                    "Invalid appointment slot"
            );
        }

        // 9. Check whether slot is already booked
        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNot(
                                doctor.getId(),
                                request.getAppointmentDate(),
                                startTime,
                                AppointmentStatus.CANCELLED
                        );

        if (alreadyBooked) {

            throw new ResourceAlreadyExistsException(
                    "Appointment slot is already booked"
            );
        }

        // 10. Create appointment
        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .startTime(startTime)
                .endTime(endTime)
                .status(AppointmentStatus.CONFIRMED)
                .build();

        Appointment savedAppointment =
                appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(savedAppointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyAppointments(
            Long userId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        ));

        return appointmentRepository
                .findByPatientIdOrderByAppointmentDateAscStartTimeAsc(
                        patient.getId()
                )
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getMyAppointment(
            Long userId,
            Long appointmentId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        ));

        Appointment appointment =
                appointmentRepository.findByIdAndPatientId(
                        appointmentId,
                        patient.getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        return appointmentMapper.toResponse(appointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(
            Long userId,
            Long appointmentId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        ));

        Appointment appointment =
                appointmentRepository.findByIdAndPatientId(
                        appointmentId,
                        patient.getId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found"
                        ));

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Appointment is already cancelled"
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new IllegalArgumentException(
                    "Completed appointment cannot be cancelled"
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDoctorAppointments(
            Long userId) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found"
                        ));

        return appointmentRepository
                .findByDoctorIdOrderByAppointmentDateAscStartTimeAsc(
                        doctor.getId()
                )
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }
    @Override
    public List<ReceptionAppointmentResponse> getTodayAppointments() {

        LocalDate today = LocalDate.now();

        return appointmentRepository
                .findByAppointmentDateOrderByStartTimeAsc(today)
                .stream()
                .map(appointment -> ReceptionAppointmentResponse.builder()
                        .appointmentId(appointment.getId())
                        .patientId(appointment.getPatient().getId())
                        .patientName(appointment.getPatient().getUser().getName())
                        .doctorId(appointment.getDoctor().getId())
                        .doctorName(appointment.getDoctor().getUser().getName())
                        .appointmentDate(appointment.getAppointmentDate())
                        .startTime(appointment.getStartTime())
                        .endTime(appointment.getEndTime())
                        .status(appointment.getStatus())
                        .build())
                .toList();
    }
}