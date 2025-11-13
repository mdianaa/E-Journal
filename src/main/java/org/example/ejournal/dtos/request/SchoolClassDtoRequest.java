package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolClassDtoRequest {

    @NotBlank
    @Size(max = 3)
    private String className;

    @NotNull
    private Long headTeacherId;

    @NotNull
    private Long schoolId;
}