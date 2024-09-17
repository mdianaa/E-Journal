package org.example.ejournal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "student_class_history")
public class StudentClassHistory extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	
	@ManyToOne
	@JoinColumn(name = "school_class_id", nullable = false)
	private SchoolClass schoolClass;
	
	@Column(name = "final_grade")
	private BigDecimal finalGrade;  // Optional: track final grades for each class
	
	@Column(name = "comments")
	private String comments;  // Optional: any additional comments or remarks
	
	
}
