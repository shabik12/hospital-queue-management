package com.hospital.hospital_queue_management.dto.request;

import com.hospital.hospital_queue_management.enums.DayOfWeek;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class DoctorScheduleRequest {

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;
    @NotNull(message = "Slot duration is required")
    @Positive(message = "Slot duration must be greater than zero")
    private Integer slotDurationMinutes;
}