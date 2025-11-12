package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BadNoteDtoRequest {

    @NotBlank
    @Size(max = 50)
    private String day;

    @NotNull
    private Long subjectId;

    @NotBlank
    private String description;

    @NotNull
    private Long studentId;

    @NotNull
    private Long teacherId;
}
