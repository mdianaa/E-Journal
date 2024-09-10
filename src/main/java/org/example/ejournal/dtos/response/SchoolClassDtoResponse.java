package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolClassDtoResponse {
	private String className;
	
	
	
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
