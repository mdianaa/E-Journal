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
public class HeadmasterDtoRequest extends BaseUserDtoRequest{
    
    @NotNull
    private Long schoolId;
    
    @NotNull
    private UserRegisterDtoRequest userRegister;
}
