package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentParentViewDtoResponse {

    private Long id;

    private String fullName;

    private Long schoolId;

    private String schoolName;
}
