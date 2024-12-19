package org.example.ejournal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "teacher_position")
public class TeacherPosition extends BaseEntity {

	@ManyToOne
	private TeacherSubject teacherSubject;
	
	@ManyToOne
	private SchoolClass schoolClass;
	
}
