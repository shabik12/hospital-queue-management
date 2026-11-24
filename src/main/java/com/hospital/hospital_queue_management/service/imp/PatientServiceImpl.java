package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.PatientRequest;
import com.hospital.hospital_queue_management.dto.response.PatientResponse;
import com.hospital.hospital_queue_management.entity.Patient;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.enums.Role;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.PatientRepository;
import com.hospital.hospital_queue_management.repository.UserRepository;
import com.hospital.hospital_queue_management.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PatientResponse createProfile(
            Long userId,
            PatientRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (user.getRole() != Role.PATIENT) {
            throw new IllegalStateException(
                    "Only patients can create a patient profile"
            );
        }

        if (patientRepository.existsByUserId(userId)) {
            throw new ResourceAlreadyExistsException(
                    "Patient profile already exists"
            );
        }

        Patient patient = Patient.builder()
                .user(user)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phone(request.getPhone())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getMyProfile(Long userId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient profile not found"
                        ));

        return mapToResponse(patient);
    }

    private PatientResponse mapToResponse(Patient patient) {

        User user = patient.getUser();

        return PatientResponse.builder()
                .id(patient.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .build();
    }
}