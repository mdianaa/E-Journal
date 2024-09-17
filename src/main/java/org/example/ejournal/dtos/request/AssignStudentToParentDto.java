package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssignStudentToParentDto {
	@NotNull(message = "Student id should not be null.")
	private long studentId;
	@NotNull(message = "Parent id should not be null.")
	private long parentId;
	
}
