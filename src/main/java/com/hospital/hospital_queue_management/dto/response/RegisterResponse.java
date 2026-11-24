package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
}