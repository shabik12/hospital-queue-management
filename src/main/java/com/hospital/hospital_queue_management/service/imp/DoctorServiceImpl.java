package com.hospital.hospital_queue_management.service.imp;

import com.hospital.hospital_queue_management.dto.request.DoctorRequest;
import com.hospital.hospital_queue_management.dto.response.DoctorResponse;
import com.hospital.hospital_queue_management.entity.Doctor;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.enums.Role;
import com.hospital.hospital_queue_management.exception.ResourceAlreadyExistsException;
import com.hospital.hospital_queue_management.exception.ResourceNotFoundException;
import com.hospital.hospital_queue_management.repository.DoctorRepository;
import com.hospital.hospital_queue_management.repository.UserRepository;
import com.hospital.hospital_queue_management.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public DoctorResponse createProfile(
            Long userId,
            DoctorRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        ));

        if (user.getRole() != Role.DOCTOR) {
            throw new IllegalStateException(
                    "Only doctors can create a doctor profile"
            );
        }

        if (doctorRepository.existsByUserId(userId)) {
            throw new ResourceAlreadyExistsException(
                    "Doctor profile already exists"
            );
        }

        if (doctorRepository.existsByLicenseNumber(
                request.getLicenseNumber())) {

            throw new ResourceAlreadyExistsException(
                    "License number already exists"
            );
        }

        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .experience(request.getExperience())
                .build();

        Doctor savedDoctor = doctorRepository.save(doctor);

        return mapToResponse(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponse getMyProfile(Long userId) {

        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor profile not found"
                        ));

        return mapToResponse(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DoctorResponse mapToResponse(Doctor doctor) {

        User user = doctor.getUser();

        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .experience(doctor.getExperience())
                .build();
    }
}