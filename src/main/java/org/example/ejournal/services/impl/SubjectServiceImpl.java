package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.services.SubjectService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class SubjectServiceImpl implements SubjectService {
	
	private final SubjectRepository subjectRepository;
	private final SchoolRepository schoolRepository;
	private final UserAuthenticationService userAuthenticationService;
	private final ModelMapper mapper;
	
	public SubjectServiceImpl(SubjectRepository subjectRepository, SchoolRepository schoolRepository, UserAuthenticationService userAuthenticationService, ModelMapper mapper) {
		this.subjectRepository = subjectRepository;
		this.schoolRepository = schoolRepository;
		this.userAuthenticationService = userAuthenticationService;
		this.mapper = mapper;
	}
	
	@Transactional
	@Override
	public SubjectDtoResponse createSubject(SubjectDtoRequest subjectDto) {
		// get the authenticated headmaster
		Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
		
		// ensure the headmaster has a school associated with them
		if (headmaster.getSchool() == null) {
			throw new NoSuchElementException("No school found for the authenticated headmaster");
		}
		
		//  check whether the subject already exists
		if (subjectRepository.findByName(subjectDto.getName()).isPresent()) {
			throw new IllegalArgumentException("Subject with this name already exists");
		}
		
		// register subject
		Subject subject = mapper.map(subjectDto, Subject.class);
		subject.setSchool(headmaster.getSchool());
		
		// persist to db
		subjectRepository.save(subject);
		
		// return dto
		return mapper.map(subject, SubjectDtoResponse.class);
	}
	
	@Transactional
	@Override
	public Set<SubjectDtoResponse> viewAllSubjectsInSchool(long schoolId) {
		// Fetch the school using the provided schoolId
		School school = schoolRepository.findById(schoolId)
				.orElseThrow(() -> new NoSuchElementException("No school found with id: " + schoolId));
		
		return mapSubjectsToDto(school.getSubjects());
	}
	
	@Transactional
	@Override
	public Set<SubjectDtoResponse> viewAllSubjectsInSchool() {
		// Use the helper method to get the headmaster's school
		School school = getHeadmasterSchool();
		return mapSubjectsToDto(school.getSubjects());
	}
	
	private Set<SubjectDtoResponse> mapSubjectsToDto(Set<Subject> subjects) {
		Set<SubjectDtoResponse> subjectDtoResponses = new HashSet<>();
		for (Subject subject : subjects) {
			subjectDtoResponses.add(mapper.map(subject, SubjectDtoResponse.class));
		}
		return subjectDtoResponses;
	}
	
	private School getHeadmasterSchool() {
		// Get the authenticated headmaster
		Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
		
		// Ensure the headmaster has a school associated with them
		if (headmaster.getSchool() == null) {
			throw new NoSuchElementException("No school found for the authenticated headmaster.");
		}
		
		// Fetch the school by the headmaster's associated school ID
		return schoolRepository.findById(headmaster.getSchool().getId())
				.orElseThrow(() -> new NoSuchElementException("School of headmaster is empty."));
	}
//    @Override
//    public void deleteSubject(long schoolId, long subjectId) {
//        if (subjectRepository.findById(subjectId).isPresent()) {
//            Subject subject = subjectRepository.findById(subjectId).get();
//
//            List<Absence> absences = absenceRepository.findBySubject(subject);
//            for (Absence absence : absences) {
//                absence.setSubject(null);
//                absenceRepository.save(absence);
//            }
//
//            School school = schoolRepository.findById(schoolId).get();
//            Set<Subject> subjects = school.getSubjects().stream().filter(s -> !s.getName().equals(subject.getName())).collect(Collectors.toSet());
//            school.setSubjects(subjects);
//
//            subjectRepository.delete(subject);
//        }
//    }
}
