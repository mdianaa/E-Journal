package org.example.ejournal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "headmasters")
public class Headmaster extends User {

    @OneToOne
    private School school;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Student> students;
}
