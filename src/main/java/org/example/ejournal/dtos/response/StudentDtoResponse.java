package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDtoResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String parentFullName;

    private Long schoolClassId;

    private String schoolClassName;

    private Long schoolId;

    private String schoolName;
}
