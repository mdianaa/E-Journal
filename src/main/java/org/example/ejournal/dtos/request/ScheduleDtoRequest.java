package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDtoRequest {

    @NotBlank
    private String semester;

    @NotBlank
    private String shift;

    @NotNull
    private Long schoolClassId;

    private Set<Long> mondaySubjectIds;
    private Set<Long> tuesdaySubjectIds;
    private Set<Long> wednesdaySubjectIds;
    private Set<Long> thursdaySubjectIds;
    private Set<Long> fridaySubjectIds;
}