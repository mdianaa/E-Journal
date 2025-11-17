package org.example.ejournal.dtos.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HeadmasterDtoResponse {

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Long schoolId;

    private String schoolName;
}
