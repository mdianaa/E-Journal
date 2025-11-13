package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassDtoResponse {

    private Long id;

    private String className;

    private boolean deactivated;

    private Long schoolId;

    private String schoolName;

    private Long headTeacherId;

    private String headTeacherFullName;

    private Integer studentCount;
}
