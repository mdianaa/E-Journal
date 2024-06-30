package org.example.ejournal.services;

import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;

import java.util.Set;

public interface ParentService {

    ParentDtoRequest createParent(ParentDtoRequest parentDto, SchoolDtoRequest schoolDto, UserRegisterDtoRequest userRegisterDtoRequest);

    ParentDtoRequest editParent(long parentId, ParentDtoRequest parentDto);

    Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId);

    void deleteParent(long parentId);
}
