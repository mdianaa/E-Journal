package org.example.ejournal.dtos.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.RoleType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminRegisterDtoRequest {

    @NotNull(message = "First name shouldn't be empty")
    private String firstName;

    @NotNull(message = "Last name shouldn't be empty")
    private String lastName;

    @NotNull(message = "Phone number name shouldn't be empty")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @NotNull(message = "Username cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be empty")
    private String password;
}
