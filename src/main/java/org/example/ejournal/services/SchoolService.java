package org.example.ejournal.services;

import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.SchoolDtoResponse;
import org.example.ejournal.models.*;

import java.math.BigDecimal;
import java.util.Map;

public interface SchoolService {

    SchoolDtoRequest createSchool(SchoolDtoRequest schoolDto);

    SchoolDtoResponse viewSchoolInfo(long schoolId);

    void deleteSchool(long schoolId);

}
