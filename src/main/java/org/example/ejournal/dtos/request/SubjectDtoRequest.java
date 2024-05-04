package org.example.ejournal.dtos.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SubjectType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubjectDtoRequest {

    @NotNull(message = "Subject type shouldn't be empty")
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;
}
