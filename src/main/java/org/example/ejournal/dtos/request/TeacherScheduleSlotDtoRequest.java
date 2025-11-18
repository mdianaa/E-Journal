package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherScheduleSlotDtoRequest {

    private String day;

    private Integer periodNumber;

    private Long subjectId;

    private Long schoolClassId;
}
