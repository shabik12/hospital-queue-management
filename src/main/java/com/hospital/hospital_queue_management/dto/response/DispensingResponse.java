package com.hospital.hospital_queue_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispensingResponse {

    private Long dispensingId;
    private Long prescriptionId;
    private LocalDateTime dispensedAt;
    private List<DispensingItemResponse> items;
}