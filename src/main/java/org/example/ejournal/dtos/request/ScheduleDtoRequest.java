package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleDtoRequest {

    private WeekDay day;

    private SemesterType semester;

    // тук да се подаде в конструктора списък от всички предмети?
}
