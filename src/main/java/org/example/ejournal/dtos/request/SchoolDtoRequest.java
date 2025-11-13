package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDtoRequest {

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String address;

    @NotNull
    private Long headmasterId;

    @NotNull
    private Set<Long> teacherIds;
}