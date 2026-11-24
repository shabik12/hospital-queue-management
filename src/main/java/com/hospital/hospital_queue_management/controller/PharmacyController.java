
        package com.hospital.hospital_queue_management.controller;

import com.hospital.hospital_queue_management.api.ApiResponse;
import com.hospital.hospital_queue_management.dto.request.MedicineBatchRequest;
import com.hospital.hospital_queue_management.dto.request.MedicineRequest;
import com.hospital.hospital_queue_management.dto.request.MedicineUpdateRequest;
import com.hospital.hospital_queue_management.dto.response.DispensingResponse;
import com.hospital.hospital_queue_management.dto.response.MedicineBatchResponse;
import com.hospital.hospital_queue_management.dto.response.MedicineResponse;
import com.hospital.hospital_queue_management.dto.response.PharmacyPrescriptionResponse;
import com.hospital.hospital_queue_management.service.DispensingService;
import com.hospital.hospital_queue_management.service.MedicineBatchService;
import com.hospital.hospital_queue_management.service.MedicineService;
import com.hospital.hospital_queue_management.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PHARMACY')")
public class PharmacyController {

    private final MedicineService medicineService;
    private final MedicineBatchService medicineBatchService;
    private final PrescriptionService prescriptionService;
    private final DispensingService dispensingService;

    // =========================================================
    // MEDICINE MASTER
    // =========================================================

    @PostMapping("/medicines")
    public ResponseEntity<ApiResponse<MedicineResponse>> createMedicine(
            @Valid @RequestBody MedicineRequest request) {

        MedicineResponse medicine =
                medicineService.createMedicine(request);

        ApiResponse<MedicineResponse> response =
                ApiResponse.<MedicineResponse>builder()
                        .success(true)
                        .message("Medicine created successfully")
                        .data(medicine)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/medicines")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getAllMedicines() {

        List<MedicineResponse> medicines =
                medicineService.getAllMedicines();

        ApiResponse<List<MedicineResponse>> response =
                ApiResponse.<List<MedicineResponse>>builder()
                        .success(true)
                        .message("Medicines retrieved successfully")
                        .data(medicines)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/medicines/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> getMedicineById(
            @PathVariable Long id) {

        MedicineResponse medicine =
                medicineService.getMedicineById(id);

        ApiResponse<MedicineResponse> response =
                ApiResponse.<MedicineResponse>builder()
                        .success(true)
                        .message("Medicine retrieved successfully")
                        .data(medicine)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @PutMapping("/medicines/{id}")
    public ResponseEntity<ApiResponse<MedicineResponse>> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody MedicineUpdateRequest request) {

        MedicineResponse medicine =
                medicineService.updateMedicine(id, request);

        ApiResponse<MedicineResponse> response =
                ApiResponse.<MedicineResponse>builder()
                        .success(true)
                        .message("Medicine updated successfully")
                        .data(medicine)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/medicines/{id}/activate")
    public ResponseEntity<ApiResponse<MedicineResponse>> activateMedicine(
            @PathVariable Long id) {

        MedicineResponse medicine =
                medicineService.activateMedicine(id);

        ApiResponse<MedicineResponse> response =
                ApiResponse.<MedicineResponse>builder()
                        .success(true)
                        .message("Medicine activated successfully")
                        .data(medicine)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/medicines/{id}/deactivate")
    public ResponseEntity<ApiResponse<MedicineResponse>> deactivateMedicine(
            @PathVariable Long id) {

        MedicineResponse medicine =
                medicineService.deactivateMedicine(id);

        ApiResponse<MedicineResponse> response =
                ApiResponse.<MedicineResponse>builder()
                        .success(true)
                        .message("Medicine deactivated successfully")
                        .data(medicine)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // MEDICINE BATCH / INVENTORY
    // =========================================================

    @PostMapping("/batches")
    public ResponseEntity<ApiResponse<MedicineBatchResponse>> addBatch(
            @Valid @RequestBody MedicineBatchRequest request) {

        MedicineBatchResponse batch =
                medicineBatchService.addBatch(request);

        ApiResponse<MedicineBatchResponse> response =
                ApiResponse.<MedicineBatchResponse>builder()
                        .success(true)
                        .message("Medicine batch added successfully")
                        .data(batch)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping("/batches")
    public ResponseEntity<ApiResponse<List<MedicineBatchResponse>>> getAllBatches() {

        List<MedicineBatchResponse> batches =
                medicineBatchService.getAllBatches();

        ApiResponse<List<MedicineBatchResponse>> response =
                ApiResponse.<List<MedicineBatchResponse>>builder()
                        .success(true)
                        .message("Medicine batches retrieved successfully")
                        .data(batches)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/batches/{id}")
    public ResponseEntity<ApiResponse<MedicineBatchResponse>> getBatchById(
            @PathVariable Long id) {

        MedicineBatchResponse batch =
                medicineBatchService.getBatchById(id);

        ApiResponse<MedicineBatchResponse> response =
                ApiResponse.<MedicineBatchResponse>builder()
                        .success(true)
                        .message("Medicine batch retrieved successfully")
                        .data(batch)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/medicines/{medicineId}/batches")
    public ResponseEntity<ApiResponse<List<MedicineBatchResponse>>> getBatchesByMedicine(
            @PathVariable Long medicineId) {

        List<MedicineBatchResponse> batches =
                medicineBatchService.getBatchesByMedicine(medicineId);

        ApiResponse<List<MedicineBatchResponse>> response =
                ApiResponse.<List<MedicineBatchResponse>>builder()
                        .success(true)
                        .message("Medicine batches retrieved successfully")
                        .data(batches)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // PRESCRIPTIONS FOR PHARMACY
    // =========================================================

    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResponse<Page<PharmacyPrescriptionResponse>>>
    getPrescriptionsForPharmacy(
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable) {

        Page<PharmacyPrescriptionResponse> prescriptions =
                prescriptionService.getPrescriptionsForPharmacy(pageable);

        ApiResponse<Page<PharmacyPrescriptionResponse>> response =
                ApiResponse.<Page<PharmacyPrescriptionResponse>>builder()
                        .success(true)
                        .message("Prescriptions retrieved successfully")
                        .data(prescriptions)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/prescriptions/{prescriptionId}")
    public ResponseEntity<ApiResponse<PharmacyPrescriptionResponse>>
    getPrescriptionForPharmacy(
            @PathVariable Long prescriptionId) {

        PharmacyPrescriptionResponse prescription =
                prescriptionService.getPrescriptionForPharmacy(
                        prescriptionId
                );

        ApiResponse<PharmacyPrescriptionResponse> response =
                ApiResponse.<PharmacyPrescriptionResponse>builder()
                        .success(true)
                        .message("Prescription retrieved successfully")
                        .data(prescription)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/prescriptions/{prescriptionId}/dispense")
    public ResponseEntity<ApiResponse<DispensingResponse>> dispensePrescription(
            @PathVariable Long prescriptionId) {

        DispensingResponse dispensing =
                dispensingService.dispensePrescription(prescriptionId);

        ApiResponse<DispensingResponse> response =
                ApiResponse.<DispensingResponse>builder()
                        .success(true)
                        .message("Prescription dispensed successfully")
                        .data(dispensing)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}