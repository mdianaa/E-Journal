package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.ClassSection;
import org.example.ejournal.enums.GradeLevel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolClassDtoRequest {
    @NotNull(message = "Class grade shouldn't be empty")
    private GradeLevel gradeLevel;
    
    @NotNull(message = "Class section shouldn't be empty")
    private ClassSection classSection;
    
    @NotNull(message = "Academic year should not be empty.")
    private long academicYearId;
    
    @NotNull(message = "Teacher should not be empty.")
    private long teacherId;
}
