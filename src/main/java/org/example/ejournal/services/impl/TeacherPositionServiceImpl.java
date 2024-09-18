package org.example.ejournal.services.impl;


import org.example.ejournal.entities.TeacherPosition;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.repositories.TeacherSubjectRepository;
import org.example.ejournal.services.TeacherPositionService;
import org.springframework.stereotype.Service;

@Service
public class TeacherPositionServiceImpl implements TeacherPositionService {
	private final TeacherRepository teacherRepository;
	private final SchoolClassRepository schoolClassRepository;
	private final TeacherSubjectRepository teacherSubjectRepository;
	
	public TeacherPositionServiceImpl(TeacherRepository teacherRepository, SchoolClassRepository schoolClassRepository, TeacherSubjectRepository teacherSubjectRepository) {
		this.teacherRepository = teacherRepository;
		this.schoolClassRepository = schoolClassRepository;
		this.teacherSubjectRepository = teacherSubjectRepository;
	}
}
