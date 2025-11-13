package org.example.ejournal.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "schedules")
public class Schedule extends BaseEntity {

    @ManyToMany
    private Set<Subject> Monday;

    @OneToMany
    private Set<Subject> Tuesday;

    @OneToMany
    private Set<Subject> Wednesday;

    @OneToMany
    private Set<Subject> Thursday;

    @OneToMany
    private Set<Subject> Friday;

    @Column
    @NotNull
    private String semester;

    @Column
    @NotNull
    private String shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_class_id")
    private SchoolClass schoolClass;
}
