package org.example.ejournal.dtos.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbsenceDtoResponse {

    private Long id;

    private String day;

    private boolean excused;

    private Long studentId;

    private String studentName;

    private Long teacherId;

    private String teacherName;

    private Long subjectId;

    private String subjectName;

}
