package org.example.ejournal.services;

import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;

public interface ScheduleService {

    ScheduleDtoResponse createSchedule(ScheduleDtoRequest scheduleDto);

    ScheduleDtoResponse viewScheduleForClass(long schoolClassId);

    ScheduleDtoResponse viewScheduleForDayForClass(String day, long schoolClassId);

    void deleteSchedule(long scheduleId);

}
