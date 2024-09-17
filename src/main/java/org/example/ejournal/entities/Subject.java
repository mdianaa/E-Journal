package org.example.ejournal.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SubjectType;

import java.util.Set;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity {
    
    @Column(length = 20, nullable = false)
    private String name;
 
    @ManyToOne
    private School school;
    
    @OneToMany(mappedBy = "subject")
    private List<TeacherSubject> teacherSubjectList;
}