package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class PatientResponse {

    private Long id;
    private Long userId;
    private String name;
    private String email;
    private Role role;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
}