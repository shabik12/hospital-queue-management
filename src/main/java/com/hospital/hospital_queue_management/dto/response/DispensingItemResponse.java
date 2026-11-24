package com.hospital.hospital_queue_management.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispensingItemResponse {

    private Long medicineId;
    private String medicineName;
    private Long batchId;
    private String batchNumber;
    private Integer quantity;
}