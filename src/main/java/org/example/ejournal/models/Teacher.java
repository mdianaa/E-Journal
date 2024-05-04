package org.example.ejournal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SubjectType;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Subject> subjects;

    @ManyToOne
    private School school;

    @ManyToMany(mappedBy = "teachers", targetEntity = Student.class, fetch = FetchType.EAGER)
    private Set<Student> students;

    @Override
    public String toString() {
        return super.toString() + "Teacher{" +
                "isHeadTeacher=" + isHeadTeacher +
                ", subjects=" + subjects +
                ", school=" + school +
                ", students=" + students +
                '}';
    }
}
