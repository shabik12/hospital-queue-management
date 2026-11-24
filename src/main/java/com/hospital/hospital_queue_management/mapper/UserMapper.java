package com.hospital.hospital_queue_management.mapper;

import com.hospital.hospital_queue_management.dto.response.AdminCreateUserResponse;
import com.hospital.hospital_queue_management.dto.response.AdminDoctorResponse;
import com.hospital.hospital_queue_management.entity.Doctor;
import com.hospital.hospital_queue_management.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AdminCreateUserResponse toAdminCreateUserResponse(User user) {

        return AdminCreateUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }

    public AdminDoctorResponse toAdminDoctorResponse(Doctor doctor) {
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
}
