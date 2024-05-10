package org.example.ejournal.services;

import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.models.Parent;

import java.util.Set;

public interface ParentService {

    ParentDtoRequest createParent(ParentDtoRequest parentDto, SchoolDtoRequest schoolDto);

    ParentDtoRequest editParent(long parentId, ParentDtoRequest parentDto);

    Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId);

    void deleteParent(long parentId);
}
