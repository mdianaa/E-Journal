package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;

import java.util.List;

public interface SchoolService {

    SchoolDtoResponse createSchool(SchoolDtoRequest schoolDto);

    SchoolDtoResponse viewSchoolInfo(long schoolId);
    
    @Transactional
    List<SchoolDtoResponse> viewAllSchoolsInfo();
    
    void deleteSchool(long schoolId);

}
