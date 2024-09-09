package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.WeekDay;

import java.time.LocalDateTime;

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
    @Column
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @ManyToOne
    private Subject subject;

    @Column(columnDefinition = "boolean default false")
    private boolean isExcused;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private Student student;
}
