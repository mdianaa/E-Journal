package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserDtoRequest {
	
	@NotNull(message = "First name shouldn't be empty")
	private String firstName;
	
	@NotNull(message = "Last name shouldn't be empty")
	private String lastName;
	
	@NotNull(message = "Phone number shouldn't be empty")
	private String phoneNumber;
}