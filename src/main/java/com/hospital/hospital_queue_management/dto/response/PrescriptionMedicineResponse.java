package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.MedicineTiming;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class PrescriptionMedicineResponse {

    private Long id;
    private String medicineName;
    private String dosage;
    private Integer quantity;
    private Set<MedicineTiming> timings;
    private Integer duration;
    private String instructions;
}