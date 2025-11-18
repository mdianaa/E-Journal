package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherScheduleSlotDtoResponse {

    private Long id;

    private String dayOfWeek;

    private Integer periodNumber;

    private Long subjectId;

    private String subjectName;

    private Long schoolClassId;

    private String schoolClassName;

}
