package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "headmasters")
public class Headmaster extends User {

    @OneToOne
    private School school;

    @Column
    private LocalDateTime fromDate;
    
    @Column
    private LocalDateTime toDate;
}
