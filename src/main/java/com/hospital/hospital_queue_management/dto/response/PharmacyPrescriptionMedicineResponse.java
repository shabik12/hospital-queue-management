package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.MedicineTiming;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyPrescriptionMedicineResponse {

    private Long id;

    private String medicineName;

    private String dosage;

    private Integer quantity;

    private Set<MedicineTiming> timings;

    private Integer duration;

    private String instructions;
}