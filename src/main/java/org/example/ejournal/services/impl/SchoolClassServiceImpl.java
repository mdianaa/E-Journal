package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.dtos.response.SchoolClassDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.SchoolClassService;
import org.example.ejournal.services.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;
    private final UserAuthenticationServiceImpl userAuthenticationService;
    private final AcademicYearRepository academicYearRepository;
    public SchoolClassServiceImpl(SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository, TeacherService teacherService, SchoolRepository schoolRepository, StudentRepository studentRepository, ModelMapper mapper, UserAuthenticationServiceImpl userAuthenticationService, AcademicYearRepository academicYearRepository) {
        this.schoolClassRepository = schoolClassRepository;
        this.teacherRepository = teacherRepository;
	    this.teacherService = teacherService;
	    this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
        this.userAuthenticationService = userAuthenticationService;
	    this.academicYearRepository = academicYearRepository;
    }
    
    @Transactional
    @Override
    public SchoolClassDtoRequest createClass(SchoolClassDtoRequest schoolClassDto) {
        // Get the authenticated headmaster
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
        
        // Extract the headmaster's school
        School school = headmaster.getSchool();
        if (school == null) {
            throw new NoSuchElementException("No school found for the authenticated headmaster.");
        }
        
        // Create the SchoolClass entity from the DTO
        SchoolClass schoolClass = mapper.map(schoolClassDto, SchoolClass.class);
        
        // Set the school of the class to the headmaster's school
        schoolClass.setSchool(school);
        
        //set accademic year
        AcademicYear academicYear = academicYearRepository.findById(schoolClassDto.getAcademicYearId())
                .orElseThrow(() -> new NoSuchElementException("No such academic year with that id."));
        schoolClass.setAcademicYear(academicYear);
        
        // Find the head teacher within the school
        Teacher headTeacher = teacherRepository.findById(schoolClassDto.getTeacherId())
                .orElseThrow(() -> new NoSuchElementException("No teacher with id '" + schoolClassDto.getTeacherId() + "'"));
        
        // Check if the teacher is already the head teacher
        if (!headTeacher.isHeadTeacher()) {
            schoolClass.setHeadTeacher(headTeacher);
            headTeacher.setHeadTeacher(true);  // Set teacher as head teacher
        } else {
            // Throw an exception if the teacher is already a head teacher
            throw new IllegalArgumentException("This teacher is already set as 'headteacher'.");
        }
        
        // Save the head teacher changes
        teacherRepository.save(headTeacher);
        
        // Persist the new school class to the database
        schoolClassRepository.save(schoolClass);
        
        // Return the school class DTO (you can use a mapper to map the response if needed)
        return mapper.map(schoolClass, SchoolClassDtoRequest.class);
    }
    
    @Transactional
    @Override
    public List<SchoolClassDtoResponse> viewAllClassesHeadMaster(long academicYearId) {
        // Get the authenticated headmaster
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
        
        // Get the headmaster's school
        School school = headmaster.getSchool();
        if (school == null) {
            throw new NoSuchElementException("No school found for the authenticated headmaster.");
        }
        
        // Get the current academic year
        
        AcademicYear currentAcademicYear = academicYearRepository.findById(academicYearId)
                .orElseThrow(() -> new NoSuchElementException("No academic year was found for " + academicYearId + "/" + academicYearId + 1));
        
        // Fetch all classes for the current academic year for the headmaster's school
        List<SchoolClass> currentYearClasses = schoolClassRepository.findAllBySchoolAndAcademicYear(school, currentAcademicYear);
        
        // Map the list of school classes to a list of DTO responses
        return currentYearClasses.stream()
                .map(schoolClass -> mapper.map(schoolClass, SchoolClassDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public SchoolClassDtoRequest changeHeadTeacher(long classId, TeacherDtoRequest headTeacherDto) {
        if (schoolClassRepository.findById(classId).isPresent()) {
            //find the class by id
            SchoolClass schoolClass = schoolClassRepository.findById(classId).get();
            
            //remove the head teacher title from previous one
            teacherService.removeHeadTeacherTitle(schoolClass.getHeadTeacher().getId());
            
            //find the new headteacher
            Teacher headTeacher = teacherRepository.findByFirstNameAndLastNameAndSchoolId(headTeacherDto.getFirstName(), headTeacherDto.getLastName(),schoolClass.getSchool().getId())
                    .orElseThrow(() -> new NoSuchElementException("The teacher is not found in this school."));
            
            if (!headTeacher.isHeadTeacher()) {
                schoolClass.setHeadTeacher(headTeacher);
                headTeacher.setHeadTeacher(true);
            } else {
                // throw exception
                throw new IllegalArgumentException("This teacher is not set as 'headteacher' because he already is assigned to " + headTeacher.getSchoolClass().getGradeLevel() + ' ' + headTeacher.getSchoolClass().getClassSection());
            }
            schoolClass.setHeadTeacher(headTeacher);
            
            // persist to db
            schoolClassRepository.save(schoolClass);

            // return dto
            return mapper.map(schoolClass, SchoolClassDtoRequest.class);
        }

        return null;
    }

    //todo
    // remove boilerplate code
    private void isHeadTeacherAssigned(Teacher headTeacher){
    
    }

    @Override
    public void deleteClass(long classId) {
        if (schoolClassRepository.findById(classId).isPresent()) {
            SchoolClass schoolClass = schoolClassRepository.findById(classId).get();

            List<Student> students = studentRepository.findAllByCurrentSchoolClass(schoolClass);
            // student.setSchoolClass(null);
            studentRepository.saveAll(students);

            schoolClass.setHeadTeacher(null);
            schoolClass.setSchool(null);

            schoolClassRepository.delete(schoolClass);
        }
    }
}
