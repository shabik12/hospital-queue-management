package com.hospital.hospital_queue_management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrescriptionRequest {

    @Size(max = 500)
    private String diagnosis;

    private String notes;

    @NotEmpty
    @Valid
    private List<PrescriptionMedicineRequest> medicines;
}