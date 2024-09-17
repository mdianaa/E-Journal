package org.example.ejournal.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "academic_year")
public class AcademicYear extends BaseEntity{
	// Store the first year of the academic period as the ID (e.g., 2015 for 2015/2016)
	@Column(name = "academic_year_id", nullable = false, unique = true)
	private Integer academicYearId;  // Example: 2015 for 2015/2016
	
	@Column(name = "year_name", nullable = false)
	private String yearName;  // Example: "2015/2016"
}
