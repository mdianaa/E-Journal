package org.example.ejournal.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.enums.SubjectType;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Empty;

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
    
    @Null
    @Enumerated(EnumType.STRING)
    private RoleType role;
    
    @NotNull(message = "Username cannot be empty")
    private String username;
    
    @NotNull(message = "Password cannot be empty")
    private String password;
    
    @NotNull
    private String school;
    
}
