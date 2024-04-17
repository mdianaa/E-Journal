package org.example.ejournal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@Table(name = "students")
public class Student extends User {

    @Column(name = "personal_id", nullable = false)
    @Size(min = 10, max = 10)
    private String personalId;

    @Column(length = 50)
    private String address;

    @ManyToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @ManyToOne
    private School school;

    @OneToMany(mappedBy = "student", targetEntity = Grade.class)
    private Set<Grade> grades;

    @OneToMany(mappedBy = "student", targetEntity = Absence.class)
    private List<Absence> absences;

}
