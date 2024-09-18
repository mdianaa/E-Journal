package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SubjectWithTeachersDtoResponse {
	private SubjectDtoResponse subject;
	private List<TeacherDtoResponse> teacherName;
	
}
