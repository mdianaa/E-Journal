package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.SubjectDtoResponse;

import java.util.Set;

public interface SubjectService {

    SubjectDtoRequest createSubject(SubjectDtoRequest subjectDto, SchoolDtoRequest schoolDto);

    Set<SubjectDtoResponse> viewAllSubjectsInSchool(long schoolId);

    void deleteSubject(long schoolId, long subjectId);
}
