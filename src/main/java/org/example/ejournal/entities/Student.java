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
@Table(name = "students")
public class Student extends User {

    @Column(length = 50)
    private String address;

    @ManyToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @ManyToOne
    private School school;

    @ManyToMany(mappedBy = "students", targetEntity = Teacher.class)
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "student", targetEntity = Grade.class)
    private Set<Grade> grades;

    @OneToMany(mappedBy = "student", targetEntity = Absence.class)
    private Set<Absence> absences;

    @OneToMany(mappedBy = "student", targetEntity = BadNote.class)
    private Set<BadNote> badNotes;

    @ManyToOne
    private Parent parent;
}
