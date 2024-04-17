package org.example.ejournal.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.WeekDay;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule extends BasicEntity {

    @Enumerated
    private WeekDay day;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    @ManyToOne // first and second semester
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @ManyToMany
    private List<Subject> subjects;
}
