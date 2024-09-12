package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolDtoResponse {
    private int schoolId;
    private String name;

    private String headmasterFirstName;

    private String headmasterLastName;

}
