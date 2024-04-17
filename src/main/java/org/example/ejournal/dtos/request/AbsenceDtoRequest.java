package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.WeekDay;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AbsenceDtoRequest {

    private WeekDay weekDay;
}
