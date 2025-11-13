package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;

import java.util.Set;

public interface SubjectService {

    SubjectDtoResponse createSubject(SubjectDtoRequest subjectDto);

    Set<SubjectDtoResponse> viewAllSubjectsInSchool(long schoolId);

    void deleteSubjectInSchool(long schoolId, long subjectId);
}
