package com.hospital.hospital_queue_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineUpdateRequest {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 150, message = "Medicine name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Generic name is required")
    @Size(max = 150, message = "Generic name must not exceed 150 characters")
    private String genericName;

    @NotBlank(message = "Category is required")
    @Size(max = 100, message = "Category must not exceed 100 characters")
    private String category;

    @NotBlank(message = "Unit is required")
    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;
}