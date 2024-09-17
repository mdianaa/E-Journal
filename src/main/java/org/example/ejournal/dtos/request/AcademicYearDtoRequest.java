package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AcademicYearDtoRequest {
	@NotNull(message = "academic year id should not be null")
	@NotBlank(message = "academic year id should not be blank")
	private int academicYearId;
	
	private String yearName;

}
