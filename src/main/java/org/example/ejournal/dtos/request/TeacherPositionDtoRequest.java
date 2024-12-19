package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.TeacherSubject;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class TeacherPositionDtoRequest {
	private Set<Long> teacherSubjectIds;
	private Set<Long> schoolClassIds;
}
