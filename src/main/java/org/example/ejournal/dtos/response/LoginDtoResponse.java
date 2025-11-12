package org.example.ejournal.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDtoResponse {

    @NotNull
    private String accessToken;

    @NotNull
    private String tokenType;

    @NotNull
    private Long expiresInSeconds;
}

