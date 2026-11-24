package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DoctorResponse {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private String specialization;
    private String licenseNumber;
    private Integer experience;
}