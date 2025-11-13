package org.example.ejournal.dtos.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDtoResponse {

    private Long id;

    private String name;

    private String address;

    private Long headmasterId;

    private String headmasterFullName;

    private Integer teacherCount;

    private Integer studentCount;

    private Integer parentCount;

}
