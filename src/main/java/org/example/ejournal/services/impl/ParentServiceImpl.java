package org.example.ejournal.services.impl;

import jakarta.transaction.Transactional;
import org.example.ejournal.dtos.request.ParentDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.ParentDtoResponse;
import org.example.ejournal.entities.Parent;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.Student;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.repositories.ParentRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.StudentRepository;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.services.ParentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final ModelMapper mapper;

    public ParentServiceImpl(ParentRepository parentRepository, SchoolRepository schoolRepository, StudentRepository studentRepository, UserAuthenticationRepository userAuthenticationRepository, ModelMapper mapper) {
        this.parentRepository = parentRepository;
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.mapper = mapper;
    }

    @Override
    public ParentDtoRequest createParent(ParentDtoRequest parentDto, SchoolDtoRequest schoolDto, UserRegisterDtoRequest userRegisterDtoRequest) {
        // check if parent already exist

        // register parent
        Parent parent = mapper.map(parentDto, Parent.class);
        School school = schoolRepository.findByName(schoolDto.getName()).get();

        parent.setSchool(school);

        // map the user credentials
        UserAuthentication userAuthentication = mapper.map(userRegisterDtoRequest, UserAuthentication.class);
        parent.setUserAuthentication(userAuthentication);

        // persist to db
        userAuthenticationRepository.save(userAuthentication);
        parentRepository.save(parent);

        // return dto
        return parentDto;
    }

    @Override
    public ParentDtoRequest editParent(long parentId, ParentDtoRequest parentDto) {
        if (parentRepository.findById(parentId).isPresent()) {
            Parent parent = parentRepository.findById(parentId).get();

            // update parent entity
            mapper.map(parentDto, parent);

            // persist to db
            parentRepository.save(parent);

            // return dto
            return parentDto;
        }

        return null;
    }

    @Transactional
    @Override
    public Set<ParentDtoResponse> viewAllParentsInSchool(long schoolId) {
        School school = schoolRepository.findById(schoolId).get();

        Set<Parent> parents = school.getParents();
        Set<ParentDtoResponse> parentsDto = new HashSet<>();

        for (Parent parent : parents) {
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
            parent.setChildren(null);

            parentRepository.delete(parent);
        }
    }
}
