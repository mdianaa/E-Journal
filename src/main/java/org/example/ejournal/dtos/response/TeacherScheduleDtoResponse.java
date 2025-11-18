package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherScheduleDtoResponse {

    private Long id;

    private Long teacherId;

    private String teacherFullName;

    private String semester;

    private String shift;

    private Set<TeacherScheduleSlotDtoResponse> slots;
}
