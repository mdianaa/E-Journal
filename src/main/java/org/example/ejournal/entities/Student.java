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
    @JoinColumn(name = "current_school_class_id")  // Track the current class
    private SchoolClass currentSchoolClass;
    
    @ManyToOne
    private School school;
    
    @OneToMany(mappedBy = "student", targetEntity = Grade.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Grade> grades;
    
    @OneToMany(mappedBy = "student", targetEntity = Absence.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Absence> absences;
    
    @OneToMany(mappedBy = "student", targetEntity = BadNote.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BadNote> badNotes;
    
    @ManyToOne
    private Parent parent;
    
    // Track historical class data
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<StudentClassHistory> classHistory;
}
