package org.example.ejournal.services;

import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.models.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface ScheduleService {

    ScheduleDtoRequest createSchedule(ScheduleDtoRequest scheduleDto, SchoolClassDtoRequest schoolClassDto, Map<LocalDate, SubjectDtoRequest> subjectDtos);

    ScheduleDtoRequest visualizeSchedule(long scheduleIds);

    void deleteSchedule(long scheduleIds);

}
