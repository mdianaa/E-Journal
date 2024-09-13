package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schools")
public class School extends BaseEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 50, nullable = false)
    private String address;

    @OneToMany(mappedBy = "school", targetEntity = Teacher.class, fetch = FetchType.EAGER)
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "school", targetEntity = Headmaster.class, fetch = FetchType.EAGER)
    private List<Headmaster> headmaster;

    @OneToMany(mappedBy = "school", targetEntity = Student.class)
    private Set<Student> students;

    @OneToMany(mappedBy = "school", targetEntity = Parent.class)
    private Set<Parent> parents;
    
    @OneToMany(mappedBy = "school", fetch = FetchType.EAGER)
    private Set<Subject> subjects;
}
