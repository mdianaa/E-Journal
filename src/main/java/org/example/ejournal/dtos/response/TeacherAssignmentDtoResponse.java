package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherAssignmentDtoResponse {
	private Long teacherId;
	private String teacherName;
	private Long teacherPositionId;
	private boolean active; // TeacherPosition's active status
}
