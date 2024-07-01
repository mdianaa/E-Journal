package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BadNoteDtoResponse {

    private String description;

    private StudentDtoResponse student;

    private TeacherDtoResponse teacher;
}
