package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDtoRequest {

    @NotBlank
    @Size(max=30)
    private String firstName;

    @NotBlank
    @Size(max=30)
    private String lastName;

    @NotBlank
    @Email
    @Size(max=255)
    private String email;

    @NotBlank
    @Size(min=8, max=72)
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Long schoolId;

    @NotNull
    private Long schoolClassId;

    private Long parentId;
}
