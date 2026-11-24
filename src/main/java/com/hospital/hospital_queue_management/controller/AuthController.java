package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.LoginRequest;
import com.hospital.hospital_queue_management.dto.request.RegisterRequest;
import com.hospital.hospital_queue_management.dto.response.LoginResponse;
import com.hospital.hospital_queue_management.dto.response.RegisterResponse;
import com.hospital.hospital_queue_management.entity.User;
import com.hospital.hospital_queue_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        RegisterResponse registerResponse =
                userService.register(request);

        ApiResponse<RegisterResponse> response =
                ApiResponse.<RegisterResponse>builder()
                        .success(true)
                        .message("Registration successful")
                        .data(registerResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse loginResponse =
                userService.login(request);

        ApiResponse<LoginResponse> response =
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(loginResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<RegisterResponse>> getCurrentUser(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        RegisterResponse userResponse =
                RegisterResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build();

        ApiResponse<RegisterResponse> response =
                ApiResponse.<RegisterResponse>builder()
                        .success(true)
                        .message("Current user retrieved successfully")
                        .data(userResponse)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}