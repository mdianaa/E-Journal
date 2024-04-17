package org.example.ejournal.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SubjectType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherDtoRequest {

    private String firstName;

    private String lastName;

    private String personalId;

    private String address;

    private SubjectType qualification;
}
