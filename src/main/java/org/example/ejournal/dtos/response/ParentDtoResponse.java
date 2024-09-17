package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParentDtoResponse {

    private long id;
    private String firstName;

    private String lastName;

    private String phoneNumber;

    private List<ViewChildDto> children;

}
