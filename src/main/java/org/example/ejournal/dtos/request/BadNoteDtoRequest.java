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
public class BadNoteDtoRequest {

    @NotNull(message = "Description shouldn't be empty")
    private String description;

    @NotNull
    private SchoolClassDtoRequest schoolClass;
}
