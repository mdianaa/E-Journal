package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.WeekDay;

/**
 * The type Absence.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "absences")
public class Absence extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Student student;
}
