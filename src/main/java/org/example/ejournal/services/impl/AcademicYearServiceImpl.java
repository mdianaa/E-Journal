package org.example.ejournal.services.impl;


import org.example.ejournal.dtos.request.AcademicYearDtoRequest;
import org.example.ejournal.dtos.response.AcademicYearDtoResponse;
import org.example.ejournal.entities.AcademicYear;
import org.example.ejournal.repositories.AcademicYearRepository;
import org.example.ejournal.services.AcademicYearService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AcademicYearServiceImpl implements AcademicYearService {
	
	private final AcademicYearRepository academicYearRepository;
	private final ModelMapper mapper;
	
	public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository, ModelMapper mapper) {
		this.academicYearRepository = academicYearRepository;
		this.mapper = mapper;
	}
	
	@Override
	@Transactional
	public AcademicYearDtoResponse createAcademicYear(AcademicYearDtoRequest academicYearDto) {
		// Map DTO to entity
		AcademicYear academicYear = new AcademicYear();
		academicYear.setAcademicYearId(academicYearDto.getAcademicYearId());
		
		// Convert the Integer academicYearId to String for yearName (e.g., "2015/2016")
		String yearName = String.valueOf(academicYearDto.getAcademicYearId()) + "/" + String.valueOf(academicYearDto.getAcademicYearId() + 1);
		academicYear.setYearName(yearName);
		
		// Save the entity to the repository
		AcademicYear savedAcademicYear = academicYearRepository.save(academicYear);
		
		// Map the saved entity back to a DTO and return
		return mapper.map(savedAcademicYear, AcademicYearDtoResponse.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AcademicYearDtoResponse getAcademicYearById(Long id) {
		AcademicYear academicYear = academicYearRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Academic year not found with id: " + id));
		return mapper.map(academicYear, AcademicYearDtoResponse.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public AcademicYearDtoResponse getAcademicYearByName(String yearName) {
		AcademicYear academicYear = academicYearRepository.findByYearName(yearName)
				.orElseThrow(() -> new NoSuchElementException("Academic year not found with name: " + yearName));
		return mapper.map(academicYear, AcademicYearDtoResponse.class);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AcademicYearDtoResponse> getAllAcademicYears() {
		List<AcademicYear> academicYears = academicYearRepository.findAll();
		return academicYears.stream()
				.map(year -> mapper.map(year, AcademicYearDtoResponse.class))
				.collect(Collectors.toList());
	}
	
	@Override
	@Transactional
	public AcademicYearDtoResponse updateAcademicYear(Long id, AcademicYearDtoRequest academicYearDto) {
		AcademicYear existingYear = academicYearRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Academic year not found with id: " + id));
		
		// Update the existing entity with new data
		existingYear.setAcademicYearId(academicYearDto.getAcademicYearId());
		existingYear.setYearName(academicYearDto.getYearName());
		
		// Save the updated entity
		AcademicYear updatedYear = academicYearRepository.save(existingYear);
		
		// Map the updated entity to a DTO and return
		return mapper.map(updatedYear, AcademicYearDtoResponse.class);
	}
	
	@Transactional(readOnly = true)
	@Override
	public AcademicYearDtoResponse getLatestAcademicYear() {
		// Find the latest academic year from the repository
		AcademicYear latestYear = academicYearRepository.findLatestAcademicYear()
				.orElseThrow(() -> new NoSuchElementException("No academic year found"));
		
		// Map the entity to a DTO and return it
		return mapper.map(latestYear, AcademicYearDtoResponse.class);
	}
	
	@Override
	@Transactional
	public void deleteAcademicYear(Long id) {
		AcademicYear academicYear = academicYearRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Academic year not found with id: " + id));
		academicYearRepository.delete(academicYear);
	}
}
