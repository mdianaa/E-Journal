package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.SchoolClassDtoRequest;
import org.example.ejournal.dtos.request.TeacherDtoRequest;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.SchoolClass;
import org.example.ejournal.entities.Student;
import org.example.ejournal.entities.Teacher;
import org.example.ejournal.repositories.SchoolClassRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.TeacherRepository;
import org.example.ejournal.services.SchoolClassService;
import org.example.ejournal.services.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SchoolClassServiceImpl implements SchoolClassService {

    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public SchoolClassServiceImpl(SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository, TeacherService teacherService, SchoolRepository schoolRepository, StudentRepository studentRepository, ModelMapper mapper) {
        this.schoolClassRepository = schoolClassRepository;
        this.teacherRepository = teacherRepository;
	    this.teacherService = teacherService;
	    this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    @Override
    public SchoolClassDtoRequest createClass(SchoolClassDtoRequest schoolClassDto) {
        // check whether this class already exists
        
        // register class
        SchoolClass schoolClass = mapper.map(schoolClassDto, SchoolClass.class);
        
        School school = schoolRepository.findByName(schoolClassDto.getSchool().getName())
                .orElseThrow(()-> new NoSuchElementException("No school was found with name '"+schoolClassDto.getSchool().getName()));
        
        Teacher headTeacher = teacherRepository.findByFirstNameAndLastNameAndSchool(schoolClassDto.getTeacher().getFirstName(), schoolClassDto.getTeacher().getLastName(), school)
                .orElseThrow(()-> new NoSuchElementException("No teacher was found with the names '"+schoolClassDto.getTeacher().getFirstName()+' '+schoolClassDto.getTeacher().getLastName()+"' teaching in this school."));
        
        //check if the teacher is already headteacher
        if (!headTeacher.isHeadTeacher()) {
            schoolClass.setHeadTeacher(headTeacher);
            headTeacher.setHeadTeacher(true);
        } else {
            // throw exception
            throw new IllegalArgumentException("This teacher is not set as 'headteacher'");
        }

        teacherRepository.save(headTeacher);
        schoolClass.setSchool(school);

        // persist to db
        schoolClassRepository.save(schoolClass);

        // return dto
        return schoolClassDto;
    }

    @Override
    public SchoolClassDtoRequest changeHeadTeacher(long classId, TeacherDtoRequest headTeacherDto) {
        if (schoolClassRepository.findById(classId).isPresent()) {
            //find the class by id
            SchoolClass schoolClass = schoolClassRepository.findById(classId).get();
            
            //remove the head teacher title from previus one
            teacherService.removeHeadTeacherTitle(schoolClass.getHeadTeacher().getId());
            
            //find the new headteacher
            Teacher headTeacher = teacherRepository.findByFirstNameAndLastNameAndSchool(headTeacherDto.getFirstName(), headTeacherDto.getLastName(),schoolClass.getSchool())
                    .orElseThrow(()->new NoSuchElementException("The teacher is not found in this school."));
            
            if (!headTeacher.isHeadTeacher()) {
                schoolClass.setHeadTeacher(headTeacher);
                headTeacher.setHeadTeacher(true);
            } else {
                // throw exception
                throw new IllegalArgumentException("This teacher is not set as 'headteacher' because he already is asigned to "+ headTeacher.getSchoolClass().getClassName());
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

            List<Student> students = studentRepository.findAllBySchoolClass(schoolClass);
            for (Student student : students) {
                student.setSchoolClass(null);
                studentRepository.save(student);
            }

            schoolClass.setHeadTeacher(null);
            schoolClass.setSchool(null);

            schoolClassRepository.delete(schoolClass);
        }
    }
}
