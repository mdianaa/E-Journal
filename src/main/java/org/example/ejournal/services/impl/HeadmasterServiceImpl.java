package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.repositories.HeadmasterRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.services.HeadmasterService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class HeadmasterServiceImpl implements HeadmasterService {

    private final HeadmasterRepository headmasterRepository;
    private final SchoolRepository schoolRepository;
    private final UserAuthenticationRepository userAuthenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public HeadmasterServiceImpl(HeadmasterRepository headmasterRepository, SchoolRepository schoolRepository, UserAuthenticationRepository userAuthenticationRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.headmasterRepository = headmasterRepository;
        this.schoolRepository = schoolRepository;
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public HeadmasterDtoResponse createHeadmaster(HeadmasterDtoRequest headmasterDto, SchoolDtoRequest schoolDto, UserRegisterDtoRequest userRegisterDtoRequest) {
        // check whether the headmaster exists already
        if (userAuthenticationRepository.findByUsername(userRegisterDtoRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists!");
        }

        // check whether the school has already a headmaster
        School school = schoolRepository.findByName(schoolDto.getName()).get();
        if (school.getHeadmaster() != null) {
            throw new IllegalArgumentException("School already has a headmaster!");
        }

        // register headmaster
        Headmaster headmaster = mapper.map(headmasterDto, Headmaster.class);
        headmaster.setSchool(school);

        // map the user credentials
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUsername(userRegisterDtoRequest.getUsername());
        userAuthentication.setPassword(passwordEncoder.encode(userRegisterDtoRequest.getPassword()));
        userAuthentication.setRole(userRegisterDtoRequest.getRole());

        headmaster.setUserAuthentication(userAuthentication);

        // persist to db
        userAuthenticationRepository.save(userAuthentication);
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
