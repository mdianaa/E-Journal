package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;

import java.util.Set;

public interface ParentService {
    @Transactional
    ParentDtoResponse createParent(ParentDtoRequest parentDto);
    
    @Transactional
    Set<ParentDtoResponse> showAllParentsInClassAsHeadmaster(long classId);
    
    ParentDtoResponse editParent(long parentId, ParentDtoRequest parentDto);
    
    ParentDtoResponse viewParent(long parentId);

    Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId);

    void deleteParent(long parentId);
    
    Set<StudentDtoResponse> viewParentsChildren(long parentId);
    
    ParentDtoResponse assignStudentToParent(long parentId, long studentId);
}
