package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.request.TeacherScheduleDtoRequest;
import org.example.ejournal.dtos.request.TeacherScheduleSlotDtoRequest;
import org.example.ejournal.dtos.response.TeacherScheduleDtoResponse;
import org.example.ejournal.dtos.response.TeacherScheduleSlotDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.TeacherScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.ejournal.util.CheckExistsUtil.checkIfTeacherExists;

@Service
@RequiredArgsConstructor
public class TeacherScheduleServiceImpl implements TeacherScheduleService {

    private final TeacherScheduleRepository teacherScheduleRepository;
    private final TeacherScheduleSlotRepository slotRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;

    @Override
    @Transactional
    public TeacherScheduleDtoResponse createSchedule(TeacherScheduleDtoRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher with id: " + request.getTeacherId() + " not found"));

        TeacherSchedule schedule = teacherScheduleRepository
                .findByTeacher_IdAndSemesterAndShift(
                        request.getTeacherId(),
                        request.getSemester(),
                        request.getShift()
                )
                .orElseGet(() -> {
                    TeacherSchedule ts = new TeacherSchedule();
                    ts.setTeacher(teacher);
                    ts.setSemester(request.getSemester());
                    ts.setShift(request.getShift());
                    ts.setSlots(new LinkedHashSet<>());
                    return ts;
                });

        schedule.getSlots().clear();

        if (request.getSlots() != null) {
            for (TeacherScheduleSlotDtoRequest slotReq : request.getSlots()) {

                Subject subject = subjectRepository.findById(slotReq.getSubjectId())
                        .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + slotReq.getSubjectId()));

                SchoolClass schoolClass = schoolClassRepository.findById(slotReq.getSchoolClassId())
                        .orElseThrow(() -> new IllegalArgumentException("SchoolClass not found: " + slotReq.getSchoolClassId()));

                TeacherScheduleSlot slot = new TeacherScheduleSlot();
                slot.setTeacherSchedule(schedule);
                slot.setDay(slotReq.getDay());
                slot.setPeriodNumber(slotReq.getPeriodNumber());
                slot.setSubject(subject);
                slot.setSchoolClass(schoolClass);

                schedule.getSlots().add(slot);
            }
        }

        TeacherSchedule saved = teacherScheduleRepository.save(schedule);
        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherScheduleDtoResponse getTeacherSchedule(Long teacherId, String semester, String shift) {
        checkIfTeacherExists(teacherRepository, teacherId);

        TeacherSchedule schedule = teacherScheduleRepository
                .findByTeacher_IdAndSemesterAndShift(teacherId, semester, shift)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Schedule not found for teacher=" + teacherId +
                                ", semester=" + semester + ", shift=" + shift
                ));

        schedule.getSlots().size();

        return toDto(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<TeacherScheduleSlotDtoResponse> getDailySchedule(
            Long teacherId,
            String semester,
            String shift,
            String day
    ) {

        checkIfTeacherExists(teacherRepository, teacherId);

        List<TeacherScheduleSlot> slots = slotRepository
                .findAllByTeacherSchedule_Teacher_IdAndTeacherSchedule_SemesterAndTeacherSchedule_ShiftAndDayOrderByPeriodNumberAsc(
                        teacherId, semester, shift, day
                );

        return slots.stream()
                .map(this::toSlotDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void deleteSchedule(Long teacherId, String semester, String shift) {
        checkIfTeacherExists(teacherRepository, teacherId);

        TeacherSchedule schedule = teacherScheduleRepository
                .findByTeacher_IdAndSemesterAndShift(teacherId, semester, shift)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Schedule not found for teacher=" + teacherId +
                                ", semester=" + semester + ", shift=" + shift
                ));

        teacherScheduleRepository.delete(schedule);
    }

    private TeacherScheduleDtoResponse toDto(TeacherSchedule schedule) {
        TeacherScheduleDtoResponse dto = new TeacherScheduleDtoResponse();
        dto.setId(schedule.getId());
        dto.setTeacherId(schedule.getTeacher().getId());
        dto.setTeacherFullName(
                schedule.getTeacher().getUser().getFirstName() + " " + schedule.getTeacher().getUser().getLastName()
        );
        dto.setSemester(schedule.getSemester());
        dto.setShift(schedule.getShift());

        Set<TeacherScheduleSlotDtoResponse> slotDtos = schedule.getSlots().stream()
                .sorted(Comparator
                        .comparing(TeacherScheduleSlot::getDay)
                        .thenComparing(TeacherScheduleSlot::getPeriodNumber)
                )
                .map(this::toSlotDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        dto.setSlots(slotDtos);
        return dto;
    }

    private TeacherScheduleSlotDtoResponse toSlotDto(TeacherScheduleSlot slot) {
        TeacherScheduleSlotDtoResponse dto = new TeacherScheduleSlotDtoResponse();
        dto.setId(slot.getId());
        dto.setDayOfWeek(slot.getDay());
        dto.setPeriodNumber(slot.getPeriodNumber());

        dto.setSubjectId(slot.getSubject().getId());
        dto.setSubjectName(slot.getSubject().getName());

        dto.setSchoolClassId(slot.getSchoolClass().getId());
        dto.setSchoolClassName(slot.getSchoolClass().getClassName());

        return dto;
    }
}
