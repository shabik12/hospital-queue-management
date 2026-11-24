package com.hospital.hospital_queue_management.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyPrescriptionResponse {

    private Long prescriptionId;

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private String doctorName;

    private String diagnosis;

    private String notes;

    private List<PharmacyPrescriptionMedicineResponse> medicines;

    private LocalDateTime createdAt;
}