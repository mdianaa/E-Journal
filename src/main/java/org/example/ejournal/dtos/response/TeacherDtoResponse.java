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

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Long schoolId;

    private String schoolName;

    private boolean isHeadTeacher;

    private Long headTeacherOfClassId;

    private String headTeacherOfClassName;

    private Set<Long> subjectIds;

    private Set<String> subjectNames;

}
