package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.entities.Schedule;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.repositories.ScheduleRepository;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.services.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.ejournal.util.CheckExistsUtil.checkIfSchoolClassExists;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Override
    @Transactional
    public ScheduleDtoResponse createSchedule(ScheduleDtoRequest scheduleDto) {
        SchoolClass schoolClass = schoolClassRepository.findById(scheduleDto.getSchoolClassId())
                .orElseThrow(() -> new IllegalArgumentException("SchoolClass with id %d not found"
                        .formatted(scheduleDto.getSchoolClassId())));

        scheduleRepository.findBySchoolClass_IdAndSemesterIgnoreCaseAndShiftIgnoreCase(
                schoolClass.getId(), scheduleDto.getSemester(), scheduleDto.getShift()
        ).ifPresent(s -> {
            throw new IllegalArgumentException(
                    "Schedule already exists for class=%d, semester=%s, shift=%s"
                            .formatted(schoolClass.getId(), scheduleDto.getSemester(), scheduleDto.getShift()));
        });

        Schedule schedule = new Schedule();
        schedule.setSchoolClass(schoolClass);
        schedule.setSemester(scheduleDto.getSemester());
        schedule.setShift(scheduleDto.getShift());

        schedule.setMonday(resolveSubjects(scheduleDto.getMondaySubjectIds()));
        schedule.setTuesday(resolveSubjects(scheduleDto.getTuesdaySubjectIds()));
        schedule.setWednesday(resolveSubjects(scheduleDto.getWednesdaySubjectIds()));
        schedule.setThursday(resolveSubjects(scheduleDto.getThursdaySubjectIds()));
        schedule.setFriday(resolveSubjects(scheduleDto.getFridaySubjectIds()));

        Schedule saved = scheduleRepository.save(schedule);
        return toDto(saved);
    }

    @Override
    @Transactional
    public ScheduleDtoResponse viewScheduleForClass(long schoolClassId) {
        checkIfSchoolClassExists(schoolClassRepository, schoolClassId);

        Schedule schedule = scheduleRepository.findBySchoolClass_Id(schoolClassId)
                .orElseThrow(() -> new IllegalArgumentException("No schedule found for class id %d"
                        .formatted(schoolClassId)));
        return toDto(schedule);
    }

    @Override
    @Transactional
    public ScheduleDtoResponse viewScheduleForDayForClass(String day, long schoolClassId) {
        checkIfSchoolClassExists(schoolClassRepository, schoolClassId);

        Schedule schedule = scheduleRepository.findBySchoolClass_Id(schoolClassId)
                .orElseThrow(() -> new IllegalArgumentException("No schedule found for class id %d"
                        .formatted(schoolClassId)));

        Day normalized = normalizeDay(day);

        ScheduleDtoResponse dto = baseHeaderDto(schedule);

        switch (normalized) {
            case MONDAY -> {
                dto.setMonday(toSubjectDtos(schedule.getMonday()));
                dto.setTuesday(Set.of());
                dto.setWednesday(Set.of());
                dto.setThursday(Set.of());
                dto.setFriday(Set.of());
            }
            case TUESDAY -> {
                dto.setMonday(Set.of());
                dto.setTuesday(toSubjectDtos(schedule.getTuesday()));
                dto.setWednesday(Set.of());
                dto.setThursday(Set.of());
                dto.setFriday(Set.of());
            }
            case WEDNESDAY -> {
                dto.setMonday(Set.of());
                dto.setTuesday(Set.of());
                dto.setWednesday(toSubjectDtos(schedule.getWednesday()));
                dto.setThursday(Set.of());
                dto.setFriday(Set.of());
            }
            case THURSDAY -> {
                dto.setMonday(Set.of());
                dto.setTuesday(Set.of());
                dto.setWednesday(Set.of());
                dto.setThursday(toSubjectDtos(schedule.getThursday()));
                dto.setFriday(Set.of());
            }
            case FRIDAY -> {
                dto.setMonday(Set.of());
                dto.setTuesday(Set.of());
                dto.setWednesday(Set.of());
                dto.setThursday(Set.of());
                dto.setFriday(toSubjectDtos(schedule.getFriday()));
            }
            default -> throw new IllegalArgumentException("Unsupported day: " + day);
        }

        return dto;
    }

    @Override
    @Transactional
    public void deleteSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule with id %d not found".formatted(scheduleId)));

        scheduleRepository.delete(schedule);
    }
    private enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY }

    private Day normalizeDay(String day) {
        if (day == null) throw new IllegalArgumentException("day must not be null");
        String d = day.trim().toUpperCase(Locale.ROOT);
        return switch (d) {
            case "MONDAY", "MON" -> Day.MONDAY;
            case "TUESDAY", "TUE", "TUES" -> Day.TUESDAY;
            case "WEDNESDAY", "WED" -> Day.WEDNESDAY;
            case "THURSDAY", "THU", "THUR", "THURS" -> Day.THURSDAY;
            case "FRIDAY", "FRI" -> Day.FRIDAY;
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
    }

    private Set<Subject> resolveSubjects(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new LinkedHashSet<>();
        List<Subject> found = subjectRepository.findByIdIn(ids);

        if (found.size() != ids.size()) {
            Set<Long> foundIds = found.stream().map(Subject::getId).collect(Collectors.toSet());
            Set<Long> missing = new LinkedHashSet<>(ids);
            missing.removeAll(foundIds);
            throw new IllegalStateException("Subjects not found: " + missing);
        }

        return new LinkedHashSet<>(found);
    }

    private ScheduleDtoResponse toDto(Schedule s) {
        ScheduleDtoResponse dto = baseHeaderDto(s);
        dto.setMonday(toSubjectDtos(s.getMonday()));
        dto.setTuesday(toSubjectDtos(s.getTuesday()));
        dto.setWednesday(toSubjectDtos(s.getWednesday()));
        dto.setThursday(toSubjectDtos(s.getThursday()));
        dto.setFriday(toSubjectDtos(s.getFriday()));
        return dto;
    }

    private ScheduleDtoResponse baseHeaderDto(Schedule s) {
        ScheduleDtoResponse dto = new ScheduleDtoResponse();
        dto.setId(s.getId());
        dto.setSemester(s.getSemester());
        dto.setShift(s.getShift());
        dto.setSchoolClassId(s.getSchoolClass() != null ? s.getSchoolClass().getId() : null);
        dto.setSchoolClassName(s.getSchoolClass() != null ? s.getSchoolClass().getClassName() : null);
        return dto;
    }

    private Set<SubjectDtoResponse> toSubjectDtos(Set<Subject> set) {
        if (set == null || set.isEmpty()) return Set.of();
        return set.stream().map(this::toSubjectDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private SubjectDtoResponse toSubjectDto(Subject s) {
        SubjectDtoResponse dto = new SubjectDtoResponse();
        dto.setId(s.getId());
        dto.setName(s.getName());
        return dto;
    }
}
