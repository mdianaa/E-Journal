package org.example.ejournal.services;

import org.example.ejournal.dtos.response.ParentDtoResponse;

import java.util.Set;

public interface ParentService {

    ParentDtoResponse viewParentOfStudent(long studentId);

    Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId);

    void deleteParent(long parentId);
}
