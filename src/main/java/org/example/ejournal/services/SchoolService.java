package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;

import java.util.Set;

public interface SchoolService {

    SchoolDtoResponse createSchool(SchoolDtoRequest schoolDto);

    SchoolDtoResponse viewSchool(long schoolId);

    Set<SchoolDtoResponse> viewAllSchools();

    void deleteSchool(long schoolId);

}
