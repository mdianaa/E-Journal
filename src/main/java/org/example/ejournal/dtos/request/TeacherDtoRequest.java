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
public class TeacherDtoRequest extends BaseUserDtoRequest{
    
    @NotNull(message = "Address shouldn't be empty")
    private String address;
    
    @NotNull
    private UserRegisterDtoRequest userRegisterDtoRequest;
    
    @NotNull
    private String school;
    
}
