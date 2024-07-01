package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDtoResponse {

    private String firstName;

    private String lastName;

    private String className;

    private Set<GradeDtoResponse> grades;

    private Set<AbsenceDtoResponse> absences;

}
