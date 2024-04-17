package org.example.ejournal.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "headmasters")
public class Headmaster extends User {

    @OneToOne
    private School school;

    // трябва ли да има връзка с учениците или училището трябва да има връзка с тях
}
