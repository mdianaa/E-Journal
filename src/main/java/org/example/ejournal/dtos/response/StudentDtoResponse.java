package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.models.Absence;
import org.example.ejournal.models.Grade;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDtoResponse {  // това също да се използва за показване на информацията и на родителя и на преподавателя

    private Set<Grade> grades;

    private Set<Absence> absences;
}
