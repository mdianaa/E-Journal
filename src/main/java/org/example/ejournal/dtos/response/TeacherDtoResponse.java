package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDtoResponse {

    private String teacherFirstName;

    private String teacherLastName;

    private List<SubjectDtoResponse> subjects;

    private String schoolName;

}
