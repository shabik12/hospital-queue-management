package com.hospital.hospital_queue_management.dto.request;

import com.hospital.hospital_queue_management.enums.MedicineTiming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class PrescriptionMedicineRequest {

    @NotBlank
    @Size(max = 150)
    private String medicineName;

    @NotBlank
    @Size(max = 100)
    private String dosage;

    @Positive
    private Integer quantity;

    @NotEmpty
    private Set<MedicineTiming> timings;

    @Positive
    private Integer duration;

    private String instructions;
}