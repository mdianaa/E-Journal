package org.example.ejournal.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@Table(name = "teachers")
public class Teacher extends User {

    @Column(name = "personal_id", nullable = false)
    @Size(min = 10, max = 10)
    private String personalId;

    @Column(length = 50)
    private String address;

    @Enumerated
    private SubjectType qualification; // един учител да има повече квалификации?

    @OneToOne
    private Subject subject;  // един учител да преподава по различнипредмети?

    @ManyToMany(mappedBy = "teachers", targetEntity = School.class)
    private Set<School> schools;

}
