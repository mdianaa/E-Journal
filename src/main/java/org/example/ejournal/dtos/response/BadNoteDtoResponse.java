package org.example.ejournal.dtos.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BadNoteDtoResponse {

    private Long id;

    private String day;

    private Long subjectId;

    private String subjectName;

    private String description;

    private Long studentId;

    private String studentFullName;

    private Long teacherId;

    private String teacherFullName;
}
