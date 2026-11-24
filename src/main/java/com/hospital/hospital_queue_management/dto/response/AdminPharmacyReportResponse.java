package com.hospital.hospital_queue_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class AdminPharmacyReportResponse {

    private LocalDate reportDate;
    private Long prescriptionsIssuedToday;
    private Long pendingPharmacyPrescriptions;
    private Long dispensedPrescriptionsToday;
    private Long dispensingTransactionsToday;
}
