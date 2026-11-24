package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.DoctorScheduleRequest;
import com.hospital.hospital_queue_management.dto.response.DoctorScheduleResponse;
import com.hospital.hospital_queue_management.entity.Doctor;
import com.hospital.hospital_queue_management.entity.DoctorSchedule;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.enums.Role;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.DoctorRepository;
import com.hospital.hospital_queue_management.repository.DoctorScheduleRepository;
import com.hospital.hospital_queue_management.repository.UserRepository;
import com.hospital.hospital_queue_management.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DoctorScheduleResponse createSchedule(
            Long userId,
            DoctorScheduleRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.DOCTOR) {
            throw new IllegalStateException(
                    "Only doctors can create schedules"
            );
        }

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found"
                        ));

        if (!request.getStartTime().isBefore(
                request.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        if (request.getSlotDurationMinutes() <= 0) {

            throw new IllegalArgumentException(
                    "Slot duration must be greater than zero"
            );
        }

        long scheduleDurationMinutes =
                Duration.between(
                        request.getStartTime(),
                        request.getEndTime()
                ).toMinutes();

        if (request.getSlotDurationMinutes()
                > scheduleDurationMinutes) {

            throw new IllegalArgumentException(
                    "Slot duration cannot exceed schedule duration"
            );
        }

        if (doctorScheduleRepository
                .existsByDoctorIdAndDayOfWeek(
                        doctor.getId(),
                        request.getDayOfWeek())) {

            throw new ResourceAlreadyExistsException(
                    "Schedule already exists for this day"
            );
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDurationMinutes(
                        request.getSlotDurationMinutes()
                )
                .active(true)
                .build();

        DoctorSchedule savedSchedule =
                doctorScheduleRepository.save(schedule);

        return mapToResponse(savedSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> getMySchedules(
            Long userId) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found"
                        ));

        return doctorScheduleRepository
                .findByDoctorIdAndActiveTrue(doctor.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorScheduleResponse updateSchedule(
            Long userId,
            Long scheduleId,
            DoctorScheduleRequest request) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));

        DoctorSchedule schedule = doctorScheduleRepository
                .findByIdAndDoctorId(scheduleId, doctor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Schedule not found"));

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (request.getSlotDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Slot duration must be greater than zero");
        }

        long scheduleDurationMinutes =
                Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        if (request.getSlotDurationMinutes() > scheduleDurationMinutes) {
            throw new IllegalArgumentException("Slot duration cannot exceed schedule duration");
        }

        if (doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndIdNot(
                doctor.getId(),
                request.getDayOfWeek(),
                scheduleId)) {
            throw new ResourceAlreadyExistsException("Schedule already exists for this day");
        }

        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setSlotDurationMinutes(request.getSlotDurationMinutes());

        DoctorSchedule updated = doctorScheduleRepository.save(schedule);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long userId, Long scheduleId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor profile not found"));

        DoctorSchedule schedule = doctorScheduleRepository
                .findByIdAndDoctorId(scheduleId, doctor.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Schedule not found"));

        doctorScheduleRepository.delete(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> getDoctorSchedules(Long doctorId) {
        return doctorScheduleRepository
                .findByDoctorIdAndActiveTrue(doctorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DoctorScheduleResponse mapToResponse(
            DoctorSchedule schedule) {

        return DoctorScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctor().getId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .slotDurationMinutes(
                        schedule.getSlotDurationMinutes()
                )
                .active(schedule.getActive())
                .build();
    }
}