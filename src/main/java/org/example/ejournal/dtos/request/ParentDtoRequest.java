package org.example.ejournal.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParentDtoRequest {

    @NotBlank
    @Size(max=120) String firstName;

    @NotBlank
    @Size(max=120) String lastName;

    @NotBlank
    @Email
    @Size(max=255) String email;

    @NotBlank
    @Size(min=8, max=72)
    String password;

    @NotBlank
    @Length(max = 10)
    private String phoneNumber;

    @NotNull
    private Set<Long> childIds;
}
