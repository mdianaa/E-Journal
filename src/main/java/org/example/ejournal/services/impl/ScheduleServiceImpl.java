package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.models.Schedule;
import org.example.ejournal.models.SchoolClass;
import org.example.ejournal.models.Subject;
import org.example.ejournal.repositories.ScheduleRepository;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.services.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final ModelMapper mapper;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, SchoolClassRepository schoolClassRepository, SubjectRepository subjectRepository, ModelMapper mapper) {
        this.scheduleRepository = scheduleRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.mapper = mapper;
    }

    @Override
    public ScheduleDtoRequest createSchedule(ScheduleDtoRequest scheduleDto, SchoolClassDtoRequest schoolClassDto, Map<LocalDate, SubjectDtoRequest> subjectDtos) {
//        // check whether this schedule exists
//
//        // create schedule
//        Schedule schedule = mapper.map(scheduleDto, Schedule.class);
//        SchoolClass schoolClass = schoolClassRepository.findByClassName(schoolClassDto.getClassName()).get();
//
//        // add subjects
//        schedule.setSubjects();
//
//        for (Map.Entry<LocalDate, SubjectDtoRequest> subjectDto : subjectDtos.entrySet()) {
//            Subject subject = subjectRepository.findBySubjectType(subjectDto.getValue().getSubjectType()).get();
//            LocalDate time = subjectDto.getKey();
//
//            schedule.getSubjectsPerHours().putIfAbsent(time, subject);
//        }
//
//        // set the current school class for the schedule
//        schedule.setSchoolClass(schoolClass);
//
//        // persist to db
//        scheduleRepository.save(schedule);
//
//        // return dto
//        return scheduleDto;

        return null;
    }

    @Override
    public ScheduleDtoRequest visualizeSchedule(long scheduleIds) {
        return null;
    }

    @Override
    public void deleteSchedule(long scheduleId) {
//        if (scheduleRepository.findById(scheduleId).isPresent()) {
//            Schedule schedule = scheduleRepository.findById(scheduleId).get();
//
//            schedule.setSubjectsPerHours(null);
//            schedule.setSchoolClass(null);
//
//            scheduleRepository.delete(schedule);
//        }

        // throw exception
    }
}
