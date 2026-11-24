package com.hospital.hospital_queue_management.dto.response;

import com.hospital.hospital_queue_management.enums.DayOfWeek;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;


@Getter
@Builder
@AllArgsConstructor
public class DoctorScheduleResponse {

    private Long id;
    private Long doctorId;
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;
    private Boolean active;
}