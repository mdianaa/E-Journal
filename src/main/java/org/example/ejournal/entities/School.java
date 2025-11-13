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
@Table(name = "schools")
public class School extends BaseEntity {

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 50, nullable = false)
    private String address;

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    private Set<Teacher> teachers;

    @OneToOne(mappedBy = "school")
    private Headmaster headmaster;

    @OneToMany(mappedBy = "school")
    private Set<Student> students;

    @OneToMany(mappedBy = "school")
    private Set<Parent> parents;

    @ManyToMany
    @JoinTable(name = "school_subjects",
            joinColumns = @JoinColumn(name = "school_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;
}
