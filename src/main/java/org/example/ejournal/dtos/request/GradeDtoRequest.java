package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GradeDtoRequest {

    @NotNull
    @Digits(integer = 1, fraction = 2)
    @DecimalMin(value = "2.00")
    @DecimalMax(value = "6.00")
    private BigDecimal value;

    @NotNull
    private Long studentId;

    @NotNull
    private Long subjectId;

    @NotNull
    private Long gradedById;
}
