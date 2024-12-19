package org.example.ejournal.services;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;

public interface ScheduleService {

   // ScheduleDtoResponse createSchedule(ScheduleDtoRequest scheduleDto, SchoolClassDtoRequest schoolClassDto, SubjectDtoRequest subjectDtoRequest);
    
    @Transactional
    ScheduleDtoResponse createSchedule(ScheduleDtoRequest scheduleDtoRequest);
    
    void deleteSchedule(long scheduleId);

}
