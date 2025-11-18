package org.example.ejournal.services;

import org.example.ejournal.dtos.request.TeacherScheduleDtoRequest;
import org.example.ejournal.dtos.response.TeacherScheduleDtoResponse;
import org.example.ejournal.dtos.response.TeacherScheduleSlotDtoResponse;

import java.util.Set;

public interface TeacherScheduleService {

    // Create OR replace teacher schedule for that (teacher, semester, shift)
    TeacherScheduleDtoResponse createSchedule(TeacherScheduleDtoRequest request);

    // Get full weekly schedule
    TeacherScheduleDtoResponse getTeacherSchedule(Long teacherId, String semester, String shift);

    // Get schedule only for one day
    Set<TeacherScheduleSlotDtoResponse> getDailySchedule(
            Long teacherId,
            String semester,
            String shift,
            String dayOfWeek
    );

    // Delete a whole schedule (for that semester + shift)
    void deleteSchedule(Long teacherId, String semester, String shift);
}
