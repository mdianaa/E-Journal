package org.example.ejournal.repositories;

import org.example.ejournal.entities.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademicYearRepository extends JpaRepository<AcademicYear,Long> {
	// Find academic year by name (e.g., "2023-2024")
	Optional<AcademicYear> findByYearName(String yearName);
	Optional<AcademicYear> findById(long id);
	@Query("SELECT a FROM AcademicYear a ORDER BY a.academicYearId DESC")
	Optional<AcademicYear> findLatestAcademicYear();
	
}
