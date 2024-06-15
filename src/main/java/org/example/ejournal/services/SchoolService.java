package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;

public interface SchoolService {

    SchoolDtoRequest createSchool(SchoolDtoRequest schoolDto);

    SchoolDtoResponse viewSchoolInfo(long schoolId);

    void deleteSchool(long schoolId);

}
