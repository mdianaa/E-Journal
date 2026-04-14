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

    @NotNull()
    private String subjectName;

    @NotBlank(message = "The day cannot be left blank")
    @Size(max = 50)
    private String day;
}
