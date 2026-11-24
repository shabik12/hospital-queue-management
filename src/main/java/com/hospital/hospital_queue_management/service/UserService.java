package com.hospital.hospital_queue_management.service;

import com.hospital.hospital_queue_management.dto.request.LoginRequest;
import com.hospital.hospital_queue_management.dto.request.RegisterRequest;
import com.hospital.hospital_queue_management.dto.response.LoginResponse;
import com.hospital.hospital_queue_management.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}