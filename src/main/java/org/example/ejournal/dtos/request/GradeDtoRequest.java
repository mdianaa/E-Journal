package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GradeDtoRequest {

    @NotNull(message = "Grade value shouldn't be empty")
    @Positive(message = "Grade value should be a positive number")
    @Size(max = 3)
    private BigDecimal value;
}
