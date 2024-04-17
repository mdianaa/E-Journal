package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDtoRequest {

    private String first_name;

    private String last_name;

    private String personalId;

    private String address;

}
