package org.example.ejournal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SubjectType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "subjects")
public class Subject extends BasicEntity {

    @Enumerated(EnumType.STRING)
    private SubjectType name;

    @OneToOne(mappedBy = "subject", targetEntity = Teacher.class)
    private Teacher teacher;

}
