package org.example.ejournal.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ejournal.enums.PeriodType;
import org.example.ejournal.enums.SemesterType;
import org.example.ejournal.enums.ShiftType;
import org.example.ejournal.enums.WeekDay;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private WeekDay day;

    @Enumerated(EnumType.STRING)
    private SemesterType semester;

    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @ManyToOne(optional = false)
    private Subject subject;

    @ManyToOne(optional = false)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;

}
