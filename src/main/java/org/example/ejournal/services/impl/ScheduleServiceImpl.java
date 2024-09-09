package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.SubjectDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;
import org.example.ejournal.entities.Schedule;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.repositories.ScheduleRepository;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.services.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    public ScheduleDtoResponse createSchedule(ScheduleDtoRequest scheduleDto, SchoolClassDtoRequest schoolClassDto, SubjectDtoRequest subjectDtoRequest) {
        // check whether this schedule exists

        // create schedule
        Schedule schedule = mapper.map(scheduleDto, Schedule.class);
        SchoolClass schoolClass = schoolClassRepository.findByClassName(schoolClassDto.getClassName()).get();
        Subject subject = subjectRepository.findByName(subjectDtoRequest.getName()).get();

        // set subject
        schedule.setSubject(subject);

        // set the current school class for the schedule
        schedule.setSchoolClass(schoolClass);

        // persist to db
        scheduleRepository.save(schedule);

        // return dto
        return mapper.map(schedule, ScheduleDtoResponse.class);
    }

    @Override
    public void deleteSchedule(long scheduleId) {
        if (scheduleRepository.findById(scheduleId).isPresent()) {
            Schedule schedule = scheduleRepository.findById(scheduleId).get();

            schedule.setSubject(null);
            schedule.setSchoolClass(null);

            scheduleRepository.delete(schedule);
        }
    }
}
