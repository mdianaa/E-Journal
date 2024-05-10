package org.example.ejournal.dtos.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.ejournal.enums.*;

@AllArgsConstructor
@NotNull
@Getter
@Setter
public class ScheduleDtoResponse {

    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    private String schoolClass;

    @Enumerated(EnumType.STRING)
    private SubjectType subject;

    @Override
    public String toString() {
        return "ScheduleDtoResponse{" +
                "day=" + day +
                ", semester=" + semester +
                ", shiftType=" + shiftType +
                ", periodType=" + periodType +
                ", schoolClass='" + schoolClass + '\'' +
                ", subject=" + subject +
                '}';
    }
}
