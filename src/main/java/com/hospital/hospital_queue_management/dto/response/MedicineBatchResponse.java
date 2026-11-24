package com.hospital.hospital_queue_management.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineBatchResponse {

    private Long id;

    private Long medicineId;

    private String medicineName;

    private String batchNumber;

    private LocalDate expiryDate;

    private LocalDate purchaseDate;

    private Integer quantity;

    private Integer availableQuantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}