package org.example.ejournal.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.ShiftType;
import org.example.ejournal.enums.WeekDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule extends BasicEntity {

    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @ManyToOne
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "schedule_subjects",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects;


}
