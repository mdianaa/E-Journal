package org.example.ejournal.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "teachers")
public class Teacher extends User {
    
    @Column(name = "is_head_teacher", nullable = false, columnDefinition = "boolean default false")
    private boolean isHeadTeacher;

    @ManyToOne
    private School school;

    @OneToMany(mappedBy = "teacher")
    private List<TeacherSubject> teacherSubjectList;
    
}