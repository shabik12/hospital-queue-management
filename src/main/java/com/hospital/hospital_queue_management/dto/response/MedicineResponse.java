package com.hospital.hospital_queue_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineResponse {

    private Long id;
    private String name;
    private String genericName;
    private String category;
    private String unit;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}