package org.example.ejournal.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class AssignTeachersToSubjectsRequest {
	
	private Set<Long> teacherIds;
	private Set<Long> subjectIds;
	
}