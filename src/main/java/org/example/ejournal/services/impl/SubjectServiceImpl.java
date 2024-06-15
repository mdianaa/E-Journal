package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.entities.Absence;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.repositories.AbsenceRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.services.SubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;
    private final AbsenceRepository absenceRepository;
    private final ModelMapper mapper;

    public SubjectServiceImpl(SubjectRepository subjectRepository, SchoolRepository schoolRepository, AbsenceRepository absenceRepository, ModelMapper mapper) {
        this.subjectRepository = subjectRepository;
        this.schoolRepository = schoolRepository;
        this.absenceRepository = absenceRepository;
        this.mapper = mapper;
    }

    @Override
    public SubjectDtoRequest createSubject(SubjectDtoRequest subjectDto, SchoolDtoRequest schoolDto) {
        // check if this subject exists already

        // register subject
        Subject subject = mapper.map(subjectDto, Subject.class);
        School school = schoolRepository.findByName(schoolDto.getName()).get();

        if (school.getSubjects() == null) {
            school.setSubjects(new HashSet<>());
        }
        school.getSubjects().add(subject);

        // persist to db
        subjectRepository.save(subject);
        schoolRepository.save(school);

        // return dto
        return subjectDto;
    }

    @Override
    public Set<SubjectDtoResponse> viewAllSubjectsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();
        Set<Subject> subjects = school.getSubjects();

        Set<SubjectDtoResponse> subjectsDto = new HashSet<>();
        for (Subject subject : subjects) {
            subjectsDto.add(mapper.map(subject, SubjectDtoResponse.class));
        }

        return subjectsDto;
    }

    @Override
    public void deleteSubject(long schoolId, long subjectId) {
        if (subjectRepository.findById(subjectId).isPresent()) {
            Subject subject = subjectRepository.findById(subjectId).get();

            List<Absence> absences = absenceRepository.findBySubject(subject);
            for (Absence absence : absences) {
                absence.setSubject(null);
                absenceRepository.save(absence);
            }

            School school = schoolRepository.findById(schoolId).get();
            Set<Subject> subjects = school.getSubjects().stream().filter(s -> !s.getSubjectType().equals(subject.getSubjectType())).collect(Collectors.toSet());
            school.setSubjects(subjects);

            subjectRepository.delete(subject);
        }
    }
}
