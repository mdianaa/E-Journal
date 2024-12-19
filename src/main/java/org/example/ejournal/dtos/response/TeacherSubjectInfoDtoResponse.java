package org.example.ejournal.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherSubjectInfoDtoResponse {
	private Long teacherSubjectId; // The connection ID (from TeacherSubject)
	private Long teacherId; // Teacher ID
	private String teacherFirstName;
	private String teacherLastName;
	
	
}
