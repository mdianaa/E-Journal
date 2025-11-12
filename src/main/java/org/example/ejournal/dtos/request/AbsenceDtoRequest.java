package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbsenceDtoRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long teacherId;

    @NotNull
    private Long subjectId;

    @NotBlank
    @Size(max = 50)
    private String day;
}
