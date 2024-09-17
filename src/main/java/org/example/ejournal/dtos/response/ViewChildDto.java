package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewChildDto {
    private long id;
    
    private String username;
    
    private String firstName;

    private String lastName;
    
}
