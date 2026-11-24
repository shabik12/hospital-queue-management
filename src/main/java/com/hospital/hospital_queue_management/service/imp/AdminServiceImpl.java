package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.AdminCreateUserRequest;
import com.hospital.hospital_queue_management.dto.request.AdminDoctorCreateRequest;
import com.hospital.hospital_queue_management.dto.request.AdminDoctorUpdateRequest;
import com.hospital.hospital_queue_management.dto.request.DoctorScheduleRequest;
import com.hospital.hospital_queue_management.dto.response.AdminCreateUserResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDailyAppointmentsReportResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDashboardResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDoctorResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDoctorWorkloadReportResponse;
import com.hospital.hospital_queue_management.dto.response.AdminPharmacyReportResponse;
import com.hospital.hospital_queue_management.dto.response.DoctorScheduleResponse;
import com.hospital.hospital_queue_management.entity.Doctor;
import com.hospital.hospital_queue_management.entity.DoctorSchedule;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.enums.AppointmentStatus;
import com.hospital.hospital_queue_management.enums.PrescriptionStatus;
import com.hospital.hospital_queue_management.enums.QueueStatus;
import com.hospital.hospital_queue_management.enums.Role;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.AppointmentRepository;
import com.hospital.hospital_queue_management.repository.DoctorRepository;
import com.hospital.hospital_queue_management.repository.DoctorScheduleRepository;
import com.hospital.hospital_queue_management.repository.DispensingRepository;
import com.hospital.hospital_queue_management.repository.PrescriptionRepository;
import com.hospital.hospital_queue_management.repository.QueueEntryRepository;
import com.hospital.hospital_queue_management.repository.UserRepository;
import com.hospital.hospital_queue_management.service.AdminService;
import com.hospital.hospital_queue_management.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DispensingRepository dispensingRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // =========================
    // Staff Management
    // =========================

    @Override
    @Transactional
    public AdminCreateUserResponse createStaffUser(AdminCreateUserRequest request) {

        if (request.getRole() == Role.PATIENT || request.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException(
                    "Admin cannot create PATIENT or ADMIN users through staff management"
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return userMapper.toAdminCreateUserResponse(savedUser);
    }

    // =========================
    // Doctor Management
    // =========================

    @Override
    @Transactional
    public AdminDoctorResponse createDoctor(AdminDoctorCreateRequest request) {

        AdminCreateUserRequest userRequest = request.getUser();

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new IllegalArgumentException("License number already exists");
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.DOCTOR);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setExperience(request.getExperience());

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapDoctorToResponse(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminDoctorResponse> getDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(this::mapDoctorToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDoctorResponse getDoctor(Long doctorId) {

        Doctor doctor = getDoctorEntity(doctorId);

        return mapDoctorToResponse(doctor);
    }

    @Override
    @Transactional
    public AdminDoctorResponse updateDoctor(
            Long doctorId,
            AdminDoctorUpdateRequest request
    ) {

        Doctor doctor = getDoctorEntity(doctorId);

        if (request.getLicenseNumber() != null
                && !request.getLicenseNumber().equals(doctor.getLicenseNumber())
                && doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {

            throw new IllegalArgumentException("License number already exists");
        }

        if (request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }

        if (request.getLicenseNumber() != null) {
            doctor.setLicenseNumber(request.getLicenseNumber());
        }

        if (request.getExperience() != null) {
            doctor.setExperience(request.getExperience());
        }

        return mapDoctorToResponse(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public AdminDoctorResponse activateDoctor(Long doctorId) {

        Doctor doctor = getDoctorEntity(doctorId);

        doctor.getUser().setEnabled(true);

        return mapDoctorToResponse(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public AdminDoctorResponse deactivateDoctor(Long doctorId) {

        Doctor doctor = getDoctorEntity(doctorId);

        doctor.getUser().setEnabled(false);

        return mapDoctorToResponse(doctorRepository.save(doctor));
    }

    // =========================
    // Schedule Management
    // =========================

    @Override
    @Transactional
    public DoctorScheduleResponse createDoctorSchedule(
            Long doctorId,
            DoctorScheduleRequest request
    ) {

        Doctor doctor = getDoctorEntity(doctorId);

        if (doctorScheduleRepository.existsByDoctorIdAndDayOfWeek(
                doctorId,
                request.getDayOfWeek()
        )) {
            throw new IllegalArgumentException(
                    "Schedule already exists for this doctor and day"
            );
        }

        validateSchedule(request);

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctor(doctor);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setSlotDurationMinutes(request.getSlotDurationMinutes());
        schedule.setActive(true);

        return mapScheduleToResponse(
                doctorScheduleRepository.save(schedule)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorScheduleResponse> getDoctorSchedules(Long doctorId) {

        getDoctorEntity(doctorId);

        return doctorScheduleRepository
                .findByDoctorIdOrderByDayOfWeekAscStartTimeAsc(doctorId)
                .stream()
                .map(this::mapScheduleToResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorScheduleResponse updateDoctorSchedule(
            Long scheduleId,
            DoctorScheduleRequest request
    ) {

        DoctorSchedule schedule = doctorScheduleRepository
                .findById(scheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Schedule not found")
                );

        if (doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndIdNot(
                schedule.getDoctor().getId(),
                request.getDayOfWeek(),
                scheduleId
        )) {
            throw new IllegalArgumentException(
                    "Schedule already exists for this doctor and day"
            );
        }

        validateSchedule(request);

        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setSlotDurationMinutes(request.getSlotDurationMinutes());

        return mapScheduleToResponse(
                doctorScheduleRepository.save(schedule)
        );
    }

    @Override
    @Transactional
    public DoctorScheduleResponse activateDoctorSchedule(Long scheduleId) {

        DoctorSchedule schedule = getScheduleEntity(scheduleId);

        schedule.setActive(true);

        return mapScheduleToResponse(
                doctorScheduleRepository.save(schedule)
        );
    }

    @Override
    @Transactional
    public DoctorScheduleResponse deactivateDoctorSchedule(Long scheduleId) {

        DoctorSchedule schedule = getScheduleEntity(scheduleId);

        schedule.setActive(false);

        return mapScheduleToResponse(
                doctorScheduleRepository.save(schedule)
        );
    }

    // =========================
    // Dashboard
    // =========================

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard(LocalDate reportDate) {

        LocalDateTime start = reportDate.atStartOfDay();
        LocalDateTime end = reportDate.plusDays(1).atStartOfDay();

        return AdminDashboardResponse.builder()
                .todaysAppointments(
                        appointmentRepository.countByAppointmentDate(reportDate)
                )
                .todaysCompleted(
                        appointmentRepository.countByAppointmentDateAndStatus(
                                reportDate,
                                AppointmentStatus.COMPLETED
                        )
                )
                .todaysCancelled(
                        appointmentRepository.countByAppointmentDateAndStatus(
                                reportDate,
                                AppointmentStatus.CANCELLED
                        )
                )
                .currentWaitingPatients(
                        queueEntryRepository.countByQueueDateAndStatus(
                                reportDate,
                                QueueStatus.WAITING
                        )
                )
                .activeDoctors(
                        userRepository.countByRoleAndEnabled(
                                Role.DOCTOR,
                                true
                        )
                )
                .prescriptionsIssuedToday(
                        prescriptionRepository.countByCreatedAtBetween(
                                start,
                                end
                        )
                )
                .pendingPharmacyPrescriptions(
                        prescriptionRepository.countByStatus(
                                PrescriptionStatus.PENDING
                        )
                )
                .dispensedPrescriptionsToday(
                        dispensingRepository.countByDispensedAtBetween(
                                start,
                                end
                        )
                )
                .build();
    }

    // =========================
    // Appointment Report
    // =========================

    @Override
    @Transactional(readOnly = true)
    public AdminDailyAppointmentsReportResponse getAppointmentsReport(
            LocalDate reportDate
    ) {

        return AdminDailyAppointmentsReportResponse.builder()
                .reportDate(reportDate)
                .totalAppointments(
                        appointmentRepository.countByAppointmentDate(reportDate)
                )
                .confirmedAppointments(
                        appointmentRepository.countByAppointmentDateAndStatus(
                                reportDate,
                                AppointmentStatus.CONFIRMED
                        )
                )
                .completedAppointments(
                        appointmentRepository.countByAppointmentDateAndStatus(
                                reportDate,
                                AppointmentStatus.COMPLETED
                        )
                )
                .cancelledAppointments(
                        appointmentRepository.countByAppointmentDateAndStatus(
                                reportDate,
                                AppointmentStatus.CANCELLED
                        )
                )
                .build();
    }

    // =========================
    // Doctor Workload Report
    // =========================

    @Override
    @Transactional(readOnly = true)
    public AdminDoctorWorkloadReportResponse getDoctorWorkloadReport(
            LocalDate reportDate
    ) {

        List<AdminDoctorWorkloadReportResponse.DoctorWorkload> doctors =
                doctorRepository.findAll()
                        .stream()
                        .map(doctor -> {

                            Long doctorId = doctor.getId();

                            long totalAppointments =
                                    appointmentRepository.countByDoctorIdAndAppointmentDate(
                                            doctorId,
                                            reportDate
                                    );

                            long completedAppointments =
                                    appointmentRepository
                                            .countByDoctorIdAndAppointmentDateAndStatus(
                                                    doctorId,
                                                    reportDate,
                                                    AppointmentStatus.COMPLETED
                                            );

                            long cancelledAppointments =
                                    appointmentRepository
                                            .countByDoctorIdAndAppointmentDateAndStatus(
                                                    doctorId,
                                                    reportDate,
                                                    AppointmentStatus.CANCELLED
                                            );

                            long waitingPatients =
                                    queueEntryRepository
                                            .countByDoctorIdAndQueueDateAndStatus(
                                                    doctorId,
                                                    reportDate,
                                                    QueueStatus.WAITING
                                            );

                            return AdminDoctorWorkloadReportResponse.DoctorWorkload
                                    .builder()
                                    .doctorId(doctorId)
                                    .doctorName(doctor.getUser().getName())
                                    .specialization(doctor.getSpecialization())
                                    .totalAppointments(totalAppointments)
                                    .completedAppointments(completedAppointments)
                                    .cancelledAppointments(cancelledAppointments)
                                    .waitingPatients(waitingPatients)
                                    .build();
                        })
                        .toList();

        return AdminDoctorWorkloadReportResponse.builder()
                .reportDate(reportDate)
                .doctors(doctors)
                .build();
    }

    // =========================
    // Pharmacy Report
    // =========================

    @Override
    @Transactional(readOnly = true)
    public AdminPharmacyReportResponse getPharmacyReport(
            LocalDate reportDate
    ) {

        LocalDateTime start = reportDate.atStartOfDay();
        LocalDateTime end = reportDate.plusDays(1).atStartOfDay();

        return AdminPharmacyReportResponse.builder()
                .reportDate(reportDate)
                .prescriptionsIssuedToday(
                        prescriptionRepository.countByCreatedAtBetween(
                                start,
                                end
                        )
                )
                .pendingPharmacyPrescriptions(
                        prescriptionRepository.countByStatus(
                                PrescriptionStatus.PENDING
                        )
                )
                .dispensedPrescriptionsToday(
                        dispensingRepository.countByDispensedAtBetween(
                                start,
                                end
                        )
                )
                .dispensingTransactionsToday(
                        dispensingRepository.countByDispensedAtBetween(
                                start,
                                end
                        )
                )
                .build();
    }

    // =========================
    // Helper Methods
    // =========================

    private Doctor getDoctorEntity(Long doctorId) {

        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found")
                );
    }

    private DoctorSchedule getScheduleEntity(Long scheduleId) {

        return doctorScheduleRepository.findById(scheduleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Schedule not found")
                );
    }

    private void validateSchedule(DoctorScheduleRequest request) {

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        if (request.getSlotDurationMinutes() == null
                || request.getSlotDurationMinutes() <= 0) {
            throw new IllegalArgumentException(
                    "Slot duration must be greater than zero"
            );
        }
    }

    private AdminDoctorResponse mapDoctorToResponse(Doctor doctor) {

        User user = doctor.getUser();

        return AdminDoctorResponse.builder()
                .id(doctor.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .experience(doctor.getExperience())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }

    private DoctorScheduleResponse mapScheduleToResponse(
            DoctorSchedule schedule
    ) {

        return DoctorScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctor().getId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .slotDurationMinutes(schedule.getSlotDurationMinutes())
                .active(schedule.getActive())
                .build();
    }
}