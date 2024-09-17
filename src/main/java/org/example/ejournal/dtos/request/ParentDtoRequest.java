package org.example.ejournal.dtos.request;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.RoleType;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ParentDtoRequest extends BaseUserDtoRequest{
	
	@NotNull(message = "Registration is null.")
	private UserRegisterDtoRequest userRegisterDtoRequest;
	
	@NotNull
	private List<Long> studentIds; // Changed to List to accept multiple student IDs
}
