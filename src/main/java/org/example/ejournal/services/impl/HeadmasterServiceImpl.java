package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.repositories.HeadmasterRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.services.HeadmasterService;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class HeadmasterServiceImpl implements HeadmasterService {

    private final HeadmasterRepository headmasterRepository;
    private final SchoolRepository schoolRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final UserAuthenticationService userAuthenticationService;
    public HeadmasterServiceImpl(HeadmasterRepository headmasterRepository, SchoolRepository schoolRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper, UserAuthenticationService userAuthenticationService) {
        this.headmasterRepository = headmasterRepository;
        this.schoolRepository = schoolRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
	    this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public HeadmasterDtoResponse  createHeadmaster(HeadmasterDtoRequest headmasterDto) {
        //find the school
        School school = schoolRepository.findById(headmasterDto.getSchoolId())
                .orElseThrow(()-> new NoSuchElementException("No such school was found with id" + headmasterDto.getSchoolId()));

        // register headmaster
        Headmaster headmaster = mapper.map(headmasterDto, Headmaster.class);
        headmaster.setSchool(school);

        // map the user credentials
        UserAuthentication userAuthentication = userAuthenticationService.register(headmasterDto.getUserRegister());
        
        headmaster.setUserAuthentication(userAuthentication);

        // persist to db
        headmasterRepository.save(headmaster);
 
        // return dto
        return mapper.map(headmaster, HeadmasterDtoResponse.class);
    }

    @Override
    public HeadmasterDtoResponse viewHeadmaster(long headmasterId) {
        Headmaster headmaster = headmasterRepository.findById(headmasterId).get();

        return mapper.map(headmaster, HeadmasterDtoResponse.class);
    }
    
    @Override
    public Set<HeadmasterDtoResponse> viewAllHeadmastersInSchool(long schoolId){
            School school = schoolRepository.findById(schoolId).get();
            
            Set<Headmaster> headmasters = school.getHeadmaster();
            Set<HeadmasterDtoResponse> headmastersDto = new HashSet<>();
            
            for (Headmaster headmaster : headmasters) {
                
                headmastersDto.add(mapper.map(headmaster, HeadmasterDtoResponse.class));
            }
            
            return headmastersDto;
    }
    
    @Override
    public Set<HeadmasterDtoResponse> viewAllHeadmasters(){
        List<Headmaster> headmasters = headmasterRepository.findAll();
        Set<HeadmasterDtoResponse> headmastersDto = new HashSet<>();
        for(Headmaster headmaster : headmasters){
            headmastersDto.add(mapper.map(headmaster, HeadmasterDtoResponse.class));
        }
        return headmastersDto;
    }

    @Override
    public HeadmasterDtoResponse editHeadmaster(long headmasterId, HeadmasterDtoRequest headmasterDto) {
        if (headmasterRepository.findById(headmasterId).isPresent()) {
            Headmaster headmaster = headmasterRepository.findById(headmasterId).get();

            // change headmaster entity
            mapper.map(headmasterDto, headmaster);

            // persist to db
            headmasterRepository.save(headmaster);

            // return dto
            return mapper.map(headmaster, HeadmasterDtoResponse.class);
        }

        return null;
    }

    @Override
    public void deleteHeadmaster(long headmasterId) {
        if (headmasterRepository.findById(headmasterId).isPresent()) {
            Headmaster headmaster = headmasterRepository.findById(headmasterId).get();

            headmaster.setSchool(null);

            headmasterRepository.delete(headmaster);
        }
    }
}
