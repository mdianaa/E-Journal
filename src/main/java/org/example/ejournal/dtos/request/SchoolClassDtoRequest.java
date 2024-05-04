package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolClassDtoRequest {

    @NotNull(message = "Class name shouldn't be empty")
    private String className;
}
