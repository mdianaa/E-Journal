package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDtoResponse {

    private WeekDay day;

    private PeriodType periodType;

    private SubjectType subject;
}
