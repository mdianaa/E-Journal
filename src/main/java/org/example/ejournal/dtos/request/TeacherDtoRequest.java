package org.example.ejournal.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.enums.SubjectType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDtoRequest {

    @NotNull(message = "First name shouldn't be empty")
    private String firstName;

    @NotNull(message = "Last name shouldn't be empty")
    private String lastName;

    @NotNull(message = "Phone number shouldn't be empty")
    private String phoneNumber;

    @NotNull(message = "Address shouldn't be empty")
    private String address;
}
