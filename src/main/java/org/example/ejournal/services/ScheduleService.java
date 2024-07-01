package org.example.ejournal.services;

import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;

public interface ScheduleService {

    ScheduleDtoResponse createSchedule(ScheduleDtoRequest scheduleDto, SchoolClassDtoRequest schoolClassDto, SubjectDtoRequest subjectDtoRequest);

    ScheduleDtoResponse viewScheduleForDay(String day, String schoolClass, String semester);

    void deleteSchedule(long scheduleId);

}
