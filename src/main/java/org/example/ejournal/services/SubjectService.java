package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;

import java.util.Set;

public interface SubjectService {

    SubjectDtoResponse createSubject(SubjectDtoRequest subjectDto);

    Set<SubjectDtoResponse> viewAllSubjectsInSchool(long schoolId);
    
    @Transactional
    Set<SubjectDtoResponse> viewAllSubjectsInSchool();

//    void deleteSubject(long schoolId, long subjectId);
}
