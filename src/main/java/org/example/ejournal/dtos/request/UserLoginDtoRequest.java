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
public class UserLoginDtoRequest {

    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be null")
    private String password;
}
