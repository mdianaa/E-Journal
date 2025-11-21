package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSlotDtoResponse {

    private Long id;

    private String day;

    private int periodNumber;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long subjectId;

    private String subjectName;
}
