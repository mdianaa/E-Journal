package org.example.ejournal.entities;

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
@Table(name = "teachers")
public class Teacher extends User {

    @Column(name = "is_head_teacher", nullable = false, columnDefinition = "boolean default false")
    private boolean isHeadTeacher;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Subject> subjects;

    @ManyToOne
    private School school;

    @ManyToMany
    private Set<Student> students;
}
