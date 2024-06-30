package org.example.ejournal.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.RoleType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HeadmasterDtoRequest {

    @NotNull(message = "First name shouldn't be empty")
    private String firstName;

    @NotNull(message = "Last name shouldn't be empty")
    private String lastName;

    @NotNull(message = "Phone number shouldn't be empty")
    private String phoneNumber;
}
