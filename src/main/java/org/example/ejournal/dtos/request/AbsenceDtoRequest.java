package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Week day shouldn't be empty")
    private WeekDay weekDay;
}
