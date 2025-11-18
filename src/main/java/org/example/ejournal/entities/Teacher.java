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
public class Teacher extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "is_head_teacher", nullable = false, columnDefinition = "boolean default false")
    private boolean headTeacher;

    @OneToOne(mappedBy = "headTeacher", fetch = FetchType.LAZY)
    private SchoolClass headTeacherOf;

    @ManyToMany
    @JoinTable(name = "teacher_subjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> subjects;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @ManyToMany
    @JoinTable(name = "teacher_students",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students;
}
