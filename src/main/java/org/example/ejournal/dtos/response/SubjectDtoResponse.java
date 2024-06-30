package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubjectDtoResponse {

    private String subjectType;

    @Override
    public String toString() {
        return "SubjectDtoResponse{" +
                "subjectType='" + subjectType + '\'' +
                '}';
    }
}