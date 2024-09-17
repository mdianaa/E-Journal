package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.dtos.response.StudentDtoResponse;
import org.example.ejournal.entities.*;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.repositories.*;
import org.example.ejournal.services.ParentService;
import org.example.ejournal.services.UserAuthenticationService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    
    private final SchoolClassRepository schoolClassRepository;
    private final ModelMapper mapper;
    private final UserAuthenticationService userAuthenticationService;
    
    public ParentServiceImpl(ParentRepository parentRepository, SchoolRepository schoolRepository, StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, ModelMapper mapper, UserAuthenticationService userAuthenticationService) {
        this.parentRepository = parentRepository;
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
	    this.schoolClassRepository = schoolClassRepository;
        this.mapper = mapper;
	    this.userAuthenticationService = userAuthenticationService;
    }
    
    @Transactional
    @Override
    public ParentDtoResponse createParent(ParentDtoRequest parentDtoRequest) {
        // Retrieve the currently authenticated headmaster
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
        School headmasterSchool = headmaster.getSchool();
        
        // Fetch the students provided in the request and ensure they belong to the headmaster's school
        List<Student> students = studentRepository.findAllById(parentDtoRequest.getStudentIds());
        
        if (students.isEmpty()) {
            throw new NoSuchElementException("No students found with the provided IDs.");
        }
        
        // Ensure all students belong to the same school as the headmaster and parent is not asigned
        for (Student student : students) {
            if (student.getSchool().getId() != (headmasterSchool.getId())) {
                throw new IllegalArgumentException("The headmaster does not have permission to assign parents for students in another school.");
            }
            if (student.getParent() != null) {
                throw new IllegalArgumentException("Student with ID " + student.getId() + " already has a parent assigned.");
            }
        }
        
        // Set the role to PARENT
        parentDtoRequest.getUserRegisterDtoRequest().setRole(RoleType.PARENT);
        
        // Register user credentials via the UserAuthentication service
        UserAuthentication userAuthentication = userAuthenticationService.register(parentDtoRequest.getUserRegisterDtoRequest());
        
        // Create Parent entity and map the fields from the DTO
        Parent parent = new Parent();
        parent.setFirstName(parentDtoRequest.getFirstName());
        parent.setLastName(parentDtoRequest.getLastName());
        parent.setPhoneNumber(parentDtoRequest.getPhoneNumber());
        
        // Set the school and link UserAuthentication to the parent
        parent.setSchool(headmasterSchool);
        parent.setUserAuthentication(userAuthentication);
        
        // Assign the parent to each student
        for (Student student : students) {
            student.setParent(parent);  // Assuming the Student entity has a `setParent()` method
        }
        
        // Persist the Parent and update the associated students in the database
        Parent resultParent = parentRepository.save(parent);
        studentRepository.saveAll(students);  // Save the updated student-parent relationships
        
        // Return a ParentDtoResponse object using the mapper
        return mapper.map(resultParent, ParentDtoResponse.class);
    }
    
    
    @Transactional
    @Override
    public Set<ParentDtoResponse> showAllParentsInClassAsHeadmaster(long classId) {
        // Retrieve the currently authenticated headmaster
        Headmaster headmaster = (Headmaster) userAuthenticationService.getAuthenticatedUser();
        School headmasterSchool = headmaster.getSchool();
        
        // Fetch the school class by classId and ensure it exists
        SchoolClass schoolClass = schoolClassRepository.findById(classId)
                .orElseThrow(() -> new NoSuchElementException("Class not found with id: " + classId));
        
        // Ensure that the class belongs to the headmaster's school
        if (schoolClass.getSchool().getId() != (headmasterSchool.getId())) {
            throw new IllegalArgumentException("The headmaster does not have permission to view parents for this class.");
        }
        
        // Fetch all students in the class
        Set<Student> students = schoolClass.getStudents();
        
        // Initialize a set to store unique parents
        Set<Parent> parents = new HashSet<>();
        
        // Add each student's parent to the set, ensuring no duplicates
        for (Student student : students) {
            if (student.getParent() != null) {
                parents.add(student.getParent());
            }
        }
        
	    // Return the set of ParentDtoResponse
        return parents.stream()
                .map(parent -> mapper.map(parent, ParentDtoResponse.class))
                .collect(Collectors.toSet());
    }
    
    @Transactional
    @Override
    public ParentDtoResponse editParent(long parentId, ParentDtoRequest parentDto) {
        // Fetch the parent entity by parentId
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found with id: " + parentId));
        
        // Retrieve the authenticated user (either admin or headmaster)
        User authenticatedUser = userAuthenticationService.getAuthenticatedUser();
        
        // If the authenticated user is not an admin, check that the headmaster has permission
        if (!authenticatedUser.getUserAuthentication().getRole().equals(RoleType.ADMIN)) {
            // Cast to Headmaster and check if they belong to the same school as the parent
            Headmaster headmaster = (Headmaster) authenticatedUser;
            if (headmaster.getSchool().getId() != (parent.getSchool().getId())) {
                throw new IllegalArgumentException("The headmaster does not have permission to edit this parent as they are not from the same school.");
            }
        }
        
        
        // Update the parent entity with the new details from the DTO
        mapper.map(parentDto, parent);
        
        // Handle parent-student relationships
        Set<Student> currentStudents = parent.getChildren();
        Set<Long> newStudentIds = new HashSet<>(parentDto.getStudentIds());
        
        // Unassign parent from students not in the new list
        for (Student student : currentStudents) {
            if (!newStudentIds.contains(student.getId())) {
                student.setParent(null);  // Unassign parent
                studentRepository.save(student); // Persist the change
            }
        }
        // Assign the parent to the new students in the provided list
        for (Long studentId : newStudentIds) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + studentId));
            
            if (student.getParent() != null && student.getParent().getId() !=(parent.getId())) {
                throw new IllegalArgumentException("Student with id " + studentId + " is already assigned to another parent.");
            }
            
            student.setParent(parent);  // Assign parent
            studentRepository.save(student); // Persist the change
        }
        
        // Persist the updated parent entity to the database
        Parent updatedParent = parentRepository.save(parent);
        
        // Return the updated parent as a DTO
        return mapper.map(updatedParent, ParentDtoResponse.class);
    }

    @Transactional
    @Override
    public ParentDtoResponse viewParent(long parentId) {
        Parent parent = parentRepository.findById(parentId).get();

        Hibernate.initialize(parent.getChildren());

        return mapper.map(parent, ParentDtoResponse.class);
    }
    
    @Transactional
    @Override
    public Set<StudentDtoResponse> viewParentsChildren(long parentId) {
        // Fetch the parent by ID
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found with id: " + parentId));
        
        // Fetch the currently authenticated user
        User authenticatedUser = userAuthenticationService.getAuthenticatedUser();
        
        // If the authenticated user is a headmaster, ensure the parent belongs to the same school
        if (authenticatedUser.getUserAuthentication().getRole().equals(RoleType.HEADMASTER)) {
            Headmaster headmaster = (Headmaster) authenticatedUser;
            if (parent.getSchool().getId() != (headmaster.getSchool().getId())) {
                throw new IllegalArgumentException("The headmaster does not have permission to view children of parents from another school.");
            }
        }
        
        // Fetch all children associated with the parent
        Set<Student> children = parent.getChildren();
        
        // Map the students to StudentDtoResponse and return them
        return children.stream()
                .map(student -> mapper.map(student, StudentDtoResponse.class))
                .collect(Collectors.toSet());
    }
    
    @Transactional
    @Override
    public ParentDtoResponse assignStudentToParent(long parentId, long studentId) {
        // Fetch the parent entity by parentId
        Parent parent = parentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("Parent not found with id: " + parentId));
        
        // Fetch the student entity by studentId
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id: " + studentId));
        
        // Retrieve the authenticated user
        User authenticatedUser = userAuthenticationService.getAuthenticatedUser();
        
        // Check if the user is a headmaster and ensure the student belongs to their school
        if (authenticatedUser.getUserAuthentication().getRole().equals(RoleType.HEADMASTER)) {
            Headmaster headmaster = (Headmaster) authenticatedUser;
            if (student.getSchool().getId() !=(headmaster.getSchool().getId())) {
                throw new IllegalArgumentException("Headmasters can only assign students from their own school.");
            }
        }
        
        // Assign the parent to the student
        student.setParent(parent);
        
        // Persist the updated student entity
        studentRepository.save(student);
        
        // Return the updated parent DTO
        return mapper.map(parent, ParentDtoResponse.class);
    }
    
    
    @Transactional
    @Override
    public Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Parent> parents = school.getParents();
        Set<ParentDtoResponse> parentsDto = new HashSet<>();

        for (Parent parent : parents) {
            Hibernate.initialize(parent.getChildren());

            parentsDto.add(mapper.map(parent, ParentDtoResponse.class));
        }

        return parentsDto;
    }

    @Override
    public void deleteParent(long parentId) {
        if (parentRepository.findById(parentId).isPresent()) {
            Parent parent = parentRepository.findById(parentId).get();

            List<Student> children = studentRepository.findAllByParent(parent);
            for (Student child : children) {
                child.setParent(null);
                studentRepository.save(child);
            }

            parent.setChildren(null);
            parent.setSchool(null);

            parentRepository.delete(parent);
        }
    }
}
