package org.example.ejournal.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.models.Parent;
import org.example.ejournal.models.Student;
import org.example.ejournal.models.Subject;
import org.example.ejournal.models.Teacher;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SchoolDtoResponse {

    private String name;

    private String address;

    private HeadmasterDtoResponse headmaster;

    @Override
    public String toString() {
        return "SchoolDtoResponse{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", headmaster='" + headmaster + '\'' +
                '}';
    }
}
