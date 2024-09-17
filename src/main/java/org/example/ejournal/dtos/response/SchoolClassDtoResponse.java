package org.example.ejournal.dtos.response;

import jakarta.persistence.Enumerated;
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
public class SchoolClassDtoResponse {
	private GradeLevel gradeLevel;
	
	private ClassSection classSection;
	
	private long teacherId;
	
	private long academicYearId;
	//teacher names
	private String headTeacherFirstName;
	private String headTeacherLastName;
	
	
	
	
	/*
	{
    "className": "1A",
    "teacher": {
        "firstName": "Ivan",
        "lastName": "Ivanov",
        "phoneNumber": null,
        "address": null,
        "role": null,
        "username": null,
        "password": null,
        "school": null
    },
    "school": {
        "name": "9feg",
        "address": null
    }
}
	*/
}
