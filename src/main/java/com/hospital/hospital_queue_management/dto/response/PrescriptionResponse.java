package com.hospital.hospital_queue_management.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PrescriptionResponse {

    private Long id;
    private Long patientId;
    private String doctorName;
    private String diagnosis;
    private String notes;
    private List<PrescriptionMedicineResponse> medicines;
    private LocalDateTime createdAt;
}