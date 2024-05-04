package org.example.ejournal.dtos.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.ShiftType;
import org.example.ejournal.enums.WeekDay;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDtoRequest {

    @NotNull(message = "Day shouldn't be empty")
    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @NotNull(message = "Semester shouldn't be empty")
    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    @NotNull(message = "Shift shouldn't be empty")
    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    // тук да се подаде в конструктора списък от всички предмети? - как ще се задава програмата?
}
