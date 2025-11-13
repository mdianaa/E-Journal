package org.example.ejournal.dtos.response;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDtoResponse {

    private Long id;

    private String semester;

    private String shift;

    private Long schoolClassId;

    private String schoolClassName;

    private Set<SubjectDtoResponse> monday;
    private Set<SubjectDtoResponse> tuesday;
    private Set<SubjectDtoResponse> wednesday;
    private Set<SubjectDtoResponse> thursday;
    private Set<SubjectDtoResponse> friday;
}