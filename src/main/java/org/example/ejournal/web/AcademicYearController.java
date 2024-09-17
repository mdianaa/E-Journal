package org.example.ejournal.web;

import org.example.ejournal.dtos.request.AcademicYearDtoRequest;
import org.example.ejournal.dtos.response.AcademicYearDtoResponse;
import org.example.ejournal.services.AcademicYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/academic-years")
public class AcademicYearController {
	
	private final AcademicYearService academicYearService;
	
	public AcademicYearController(AcademicYearService academicYearService) {
		this.academicYearService = academicYearService;
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createAcademicYear(@RequestBody AcademicYearDtoRequest academicYearDto) {
		try {
			AcademicYearDtoResponse createdAcademicYear = academicYearService.createAcademicYear(academicYearDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdAcademicYear);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating academic year: " + e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAcademicYearById(@PathVariable Long id) {
		try {
			AcademicYearDtoResponse academicYear = academicYearService.getAcademicYearById(id);
			return ResponseEntity.ok(academicYear);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Academic year not found with id: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving academic year: " + e.getMessage());
		}
	}
	
	@GetMapping("/by-name")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAcademicYearByName(@RequestParam String yearName) {
		try {
			AcademicYearDtoResponse academicYear = academicYearService.getAcademicYearByName(yearName);
			return ResponseEntity.ok(academicYear);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Academic year not found with name: " + yearName);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving academic year: " + e.getMessage());
		}
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','HEADMASTER','TEACHER','STUDENT')")
	public ResponseEntity<?> getAllAcademicYears() {
		try {
			List<AcademicYearDtoResponse> academicYears = academicYearService.getAllAcademicYears();
			return ResponseEntity.ok(academicYears);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving academic years: " + e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	
	public ResponseEntity<?> updateAcademicYear(@PathVariable Long id, @RequestBody AcademicYearDtoRequest academicYearDto) {
		try {
			AcademicYearDtoResponse updatedAcademicYear = academicYearService.updateAcademicYear(id, academicYearDto);
			return ResponseEntity.ok(updatedAcademicYear);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Academic year not found with id: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating academic year: " + e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteAcademicYear(@PathVariable Long id) {
		try {
			academicYearService.deleteAcademicYear(id);
			return ResponseEntity.noContent().build();
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Academic year not found with id: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting academic year: " + e.getMessage());
		}
	}
}
