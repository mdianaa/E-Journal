package org.example.ejournal.models;

import jakarta.persistence.*;
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
@Table(name = "subjects")
public class Subject extends BasicEntity {

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    @ManyToMany(mappedBy = "subjects", targetEntity = Teacher.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Teacher> teachers;
}
