package org.example.ejournal.dtos.response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeDtoResponse {

    private Long id;

    private BigDecimal value;

    private Long studentId;

    private String studentName;

    private Long subjectId;

    private String subjectName;

    private Long gradedById;

    private String gradedByName;

}
