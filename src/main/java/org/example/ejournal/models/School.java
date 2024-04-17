package org.example.ejournal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schools")
public class School extends BasicEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 50, nullable = false)
    private String address;

    @ManyToMany
    private Set<Teacher> teachers;

    @OneToOne(mappedBy = "school", targetEntity = Headmaster.class)
    private Headmaster headmaster;

    @OneToMany(mappedBy = "school", targetEntity = Student.class)
    private Set<Student> students;
}
