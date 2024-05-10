package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GradeDtoResponse {

    private BigDecimal value;

    private String subjectType;

    private TeacherDtoResponse gradedByTeacher;

    @Override
    public String toString() {
        return "GradeDtoResponse{" +
                "value=" + value +
                ", subject='" + subjectType + '\'' +
                ", gradedByTeacher='" + gradedByTeacher + '\'' +
                '}';
    }
}
