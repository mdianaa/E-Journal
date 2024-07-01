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
@Table(name = "school_classes")
public class SchoolClass extends BaseEntity {

    @Column(length = 3, nullable = false)
    private String className;

    @OneToOne(optional = false)
    @JoinColumn(name = "head_teacher_id")
    private Teacher headTeacher;

    @OneToMany(mappedBy = "schoolClass", targetEntity = Student.class, fetch = FetchType.EAGER)
    private Set<Student> students;

    @ManyToOne
    private School school;

}
