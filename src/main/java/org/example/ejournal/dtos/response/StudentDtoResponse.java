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
public class StudentDtoResponse {  // това също да се използва за показване на информацията и на родителя и на преподавателя

    private String firstName;

    private String lastName;

    private String className;

    private Set<GradeDtoResponse> grades;

    private Set<AbsenceDtoResponse> absences;

    @Override
    public String toString() {
        return "StudentDtoResponse{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", className='" + className + '\'' +
                ", grades=" + grades +
                ", absences=" + absences +
                '}';
    }
}
