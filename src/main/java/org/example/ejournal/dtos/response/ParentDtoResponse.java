package org.example.ejournal.dtos.response;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParentDtoResponse {

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Set<StudentParentViewDtoResponse> children;

}
