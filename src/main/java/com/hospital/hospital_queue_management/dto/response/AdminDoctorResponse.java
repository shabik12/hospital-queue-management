package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminDoctorResponse {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private Boolean enabled;
    private String specialization;
    private String licenseNumber;
    private Integer experience;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
