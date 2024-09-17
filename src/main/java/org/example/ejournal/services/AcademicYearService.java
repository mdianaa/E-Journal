package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AcademicYearDtoRequest;
import org.example.ejournal.dtos.response.AcademicYearDtoResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AcademicYearService {
	@Transactional
	AcademicYearDtoResponse createAcademicYear(AcademicYearDtoRequest academicYearDto);
	
	@Transactional(readOnly = true)
	AcademicYearDtoResponse getAcademicYearById(Long id);
	
	@Transactional(readOnly = true)
	AcademicYearDtoResponse getAcademicYearByName(String yearName);
	
	@Transactional(readOnly = true)
	List<AcademicYearDtoResponse> getAllAcademicYears();
	
	@Transactional
	AcademicYearDtoResponse updateAcademicYear(Long id, AcademicYearDtoRequest academicYearDto);
	
	@Transactional(readOnly = true)
	AcademicYearDtoResponse getLatestAcademicYear();
	
	@Transactional
	void deleteAcademicYear(Long id);
}
