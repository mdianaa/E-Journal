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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDtoRequest extends BaseUserDtoRequest{
    
    @NotNull(message = "Address shouldn't be empty")
    private String address;
    
    @NotNull(message = "School class id should not be empty.")
    private long schoolClassId;
    
    private UserRegisterDtoRequest userRegisterDtoRequest;
}
