package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubjectDtoResponse {
    private Long id;
    private String name;
    private String schoolName;
//    private String phoneNumber;
//    private List<TeacherSubjectInfoDtoResponse> teacherAssignments; // List of teacher assignments
//
    // Getters and Setters
}

