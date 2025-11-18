package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherScheduleDtoRequest {

    private Long teacherId;

    private String semester;

    private String shift;

    private Set<TeacherScheduleSlotDtoRequest> slots;
}
