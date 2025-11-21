package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.ScheduleDtoRequest;
import org.example.ejournal.dtos.request.ScheduleSlotDtoRequest;
import org.example.ejournal.dtos.response.ScheduleDtoResponse;
import org.example.ejournal.dtos.response.ScheduleSlotDtoResponse;
import org.example.ejournal.dtos.response.SubjectDtoResponse;
import org.example.ejournal.entities.Schedule;
import org.example.ejournal.entities.ScheduleSlot;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Subject;
import org.example.ejournal.repositories.ScheduleRepository;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SubjectRepository;
import org.example.ejournal.services.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
                .orElseThrow(() -> new IllegalArgumentException(
                        "SchoolClass with id %d not found".formatted(scheduleDto.getSchoolClassId())
                ));

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

        Set<ScheduleSlot> slots = new LinkedHashSet<>();
        if (scheduleDto.getSlots() != null) {
            for (ScheduleSlotDtoRequest slotReq : scheduleDto.getSlots()) {
                ScheduleSlot slot = toSlotEntity(schedule, slotReq);
                slots.add(slot);
            }
        }
        schedule.setSlots(slots);

        Schedule saved = scheduleRepository.save(schedule);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDtoResponse viewScheduleForClass(long schoolClassId) {
        checkIfSchoolClassExists(schoolClassRepository, schoolClassId);

        Schedule schedule = scheduleRepository.findBySchoolClass_Id(schoolClassId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No schedule found for class id %d".formatted(schoolClassId)
                ));

        return toDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleDtoResponse viewScheduleForDayForClass(String day, long schoolClassId) {
        checkIfSchoolClassExists(schoolClassRepository, schoolClassId);

        Schedule schedule = scheduleRepository.findBySchoolClass_Id(schoolClassId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No schedule found for class id %d".formatted(schoolClassId)
                ));

        Day normalized = normalizeDay(day);

        ScheduleDtoResponse dto = baseHeaderDto(schedule);

        Set<ScheduleSlotDtoResponse> daySlots = schedule.getSlots().stream()
                .filter(slot -> normalized.name().equals(slot.getDay()))
                .sorted((a, b) -> Integer.compare(a.getPeriodNumber(), b.getPeriodNumber()))
                .map(this::toSlotDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setSlots(daySlots);
        return dto;
    }

    @Override
    @Transactional
    public void deleteSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Schedule with id %d not found".formatted(scheduleId)));

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

    private ScheduleSlot toSlotEntity(Schedule schedule, ScheduleSlotDtoRequest req) {
        if (req.getSubjectId() == null) {
            throw new IllegalArgumentException("subjectId must not be null for a schedule slot");
        }

        Subject subject = subjectRepository.findById(req.getSubjectId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Subject with id %d not found".formatted(req.getSubjectId())
                ));

        Day normalized = normalizeDay(req.getDay());

        ScheduleSlot slot = new ScheduleSlot();
        slot.setSchedule(schedule);
        slot.setDay(normalized.name()); // store "MONDAY"
        slot.setPeriodNumber(req.getPeriodNumber());
        slot.setStartTime(req.getStartTime());
        slot.setEndTime(req.getEndTime());
        slot.setSubject(subject);
        return slot;
    }

    private ScheduleDtoResponse toDto(Schedule s) {
        ScheduleDtoResponse dto = baseHeaderDto(s);

        Set<ScheduleSlotDtoResponse> slotDtos = s.getSlots().stream()
                .sorted(Comparator.comparing(ScheduleSlot::getDay).thenComparingInt(ScheduleSlot::getPeriodNumber))
                .map(this::toSlotDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setSlots(slotDtos);
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

    private ScheduleSlotDtoResponse toSlotDto(ScheduleSlot slot) {
        ScheduleSlotDtoResponse dto = new ScheduleSlotDtoResponse();
        dto.setId(slot.getId());
        dto.setDay(slot.getDay());
        dto.setPeriodNumber(slot.getPeriodNumber());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        dto.setSubjectId(slot.getSubject().getId());
        dto.setSubjectName(slot.getSubject().getName());
        return dto;
    }
}
