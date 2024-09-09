package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.entities.TeacherSubject;

import java.util.Set;

public interface TeacherSubjectService {
	TeacherSubject assignTeacherToSubjectClass(Long teacherId, Long subjectId);
	
	@Transactional
	Set<TeacherSubject> assignMultipleTeacherToSubjectClass(Set<Long> teacherId, Long subjectId);
}
