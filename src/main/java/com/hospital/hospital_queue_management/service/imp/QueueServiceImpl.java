
package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.response.QueueEntryResponse;
import com.hospital.hospital_queue_management.entity.Appointment;
import com.hospital.hospital_queue_management.entity.Doctor;
import com.hospital.hospital_queue_management.entity.Patient;
import com.hospital.hospital_queue_management.entity.QueueEntry;
import com.hospital.hospital_queue_management.enums.AppointmentStatus;
import com.hospital.hospital_queue_management.enums.QueueStatus;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.AppointmentRepository;
import com.hospital.hospital_queue_management.repository.DoctorRepository;
import com.hospital.hospital_queue_management.repository.QueueEntryRepository;
import com.hospital.hospital_queue_management.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private final DoctorRepository doctorRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public QueueEntryResponse checkIn(Long appointmentId) {

        Appointment appointment =
                appointmentRepository.findById(appointmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"
                                ));

        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new IllegalArgumentException(
                    "Only confirmed appointments can be checked in"
            );
        }

        if (!appointment.getAppointmentDate().equals(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Patient can only check in on the appointment date"
            );
        }

        if (queueEntryRepository.existsByAppointmentId(appointmentId)) {
            throw new ResourceAlreadyExistsException(
                    "Patient has already checked in"
            );
        }

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();

        LocalDate today = LocalDate.now();

        Integer nextQueueNumber =
                queueEntryRepository
                        .findMaxQueueNumberByDoctorIdAndQueueDate(
                                doctor.getId(),
                                today
                        )
                        .orElse(0) + 1;

        QueueEntry queueEntry = QueueEntry.builder()
                .appointment(appointment)
                .patient(patient)
                .doctor(doctor)
                .queueDate(today)
                .queueNumber(nextQueueNumber)
                .status(QueueStatus.WAITING)
                .checkedInAt(LocalDateTime.now())
                .build();

        QueueEntry savedEntry =
                queueEntryRepository.save(queueEntry);

        return QueueEntryResponse.builder()
                .id(savedEntry.getId())
                .appointmentId(savedEntry.getAppointment().getId())
                .patientId(savedEntry.getPatient().getId())
                .queueNumber(savedEntry.getQueueNumber())
                .status(savedEntry.getStatus())
                .checkedInAt(savedEntry.getCheckedInAt())
                .calledAt(savedEntry.getCalledAt())
                .consultationStartedAt(
                        savedEntry.getConsultationStartedAt()
                )
                .completedAt(savedEntry.getCompletedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QueueEntryResponse> getTodayQueue(Long userId) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found"
                        ));

        return queueEntryRepository
                .findByDoctorIdAndQueueDateOrderByQueueNumberAsc(
                        doctor.getId(),
                        LocalDate.now()
                )
                .stream()
                .map(entry -> QueueEntryResponse.builder()
                        .id(entry.getId())
                        .appointmentId(entry.getAppointment().getId())
                        .patientId(entry.getPatient().getId())
                        .queueNumber(entry.getQueueNumber())
                        .status(entry.getStatus())
                        .checkedInAt(entry.getCheckedInAt())
                        .calledAt(entry.getCalledAt())
                        .consultationStartedAt(entry.getConsultationStartedAt())
                        .completedAt(entry.getCompletedAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public QueueEntryResponse callPatient(Long userId, Long queueEntryId) {

        QueueEntry queueEntry = queueEntryRepository
                .findByIdAndDoctorUserId(queueEntryId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue entry not found"
                        ));

        if (queueEntry.getStatus() != QueueStatus.WAITING) {
            throw new IllegalArgumentException(
                    "Only waiting patients can be called"
            );
        }

        queueEntry.setStatus(QueueStatus.CALLED);
        queueEntry.setCalledAt(LocalDateTime.now());

        QueueEntry savedEntry = queueEntryRepository.save(queueEntry);

        return QueueEntryResponse.builder()
                .id(savedEntry.getId())
                .appointmentId(savedEntry.getAppointment().getId())
                .patientId(savedEntry.getPatient().getId())
                .queueNumber(savedEntry.getQueueNumber())
                .status(savedEntry.getStatus())
                .checkedInAt(savedEntry.getCheckedInAt())
                .calledAt(savedEntry.getCalledAt())
                .consultationStartedAt(savedEntry.getConsultationStartedAt())
                .completedAt(savedEntry.getCompletedAt())
                .build();
    }
    @Override
    @Transactional
    public QueueEntryResponse startConsultation(Long userId, Long queueEntryId) {

        QueueEntry queueEntry = queueEntryRepository
                .findByIdAndDoctorUserId(queueEntryId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue entry not found"
                        ));

        if (!queueEntry.getQueueDate().equals(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Consultation can only be started on the queue date"
            );
        }

        if (queueEntry.getStatus() != QueueStatus.CALLED) {
            throw new IllegalArgumentException(
                    "Only called patients can start consultation"
            );
        }

        queueEntry.setStatus(QueueStatus.IN_CONSULTATION);
        queueEntry.setConsultationStartedAt(LocalDateTime.now());

        QueueEntry savedEntry = queueEntryRepository.save(queueEntry);

        return QueueEntryResponse.builder()
                .id(savedEntry.getId())
                .appointmentId(savedEntry.getAppointment().getId())
                .patientId(savedEntry.getPatient().getId())
                .queueNumber(savedEntry.getQueueNumber())
                .status(savedEntry.getStatus())
                .checkedInAt(savedEntry.getCheckedInAt())
                .calledAt(savedEntry.getCalledAt())
                .consultationStartedAt(savedEntry.getConsultationStartedAt())
                .completedAt(savedEntry.getCompletedAt())
                .build();
    }
    @Override
    @Transactional
    public QueueEntryResponse completeConsultation(Long userId, Long queueEntryId) {

        QueueEntry queueEntry = queueEntryRepository
                .findByIdAndDoctorUserId(queueEntryId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Queue entry not found"
                        ));

        if (!queueEntry.getQueueDate().equals(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Consultation can only be completed on the queue date"
            );
        }

        if (queueEntry.getStatus() != QueueStatus.IN_CONSULTATION) {
            throw new IllegalArgumentException(
                    "Only ongoing consultations can be completed"
            );
        }

        queueEntry.setStatus(QueueStatus.COMPLETED);
        queueEntry.setCompletedAt(LocalDateTime.now());

        QueueEntry savedEntry = queueEntryRepository.save(queueEntry);

        return QueueEntryResponse.builder()
                .id(savedEntry.getId())
                .appointmentId(savedEntry.getAppointment().getId())
                .patientId(savedEntry.getPatient().getId())
                .queueNumber(savedEntry.getQueueNumber())
                .status(savedEntry.getStatus())
                .checkedInAt(savedEntry.getCheckedInAt())
                .calledAt(savedEntry.getCalledAt())
                .consultationStartedAt(savedEntry.getConsultationStartedAt())
                .completedAt(savedEntry.getCompletedAt())
                .build();
    }
}
