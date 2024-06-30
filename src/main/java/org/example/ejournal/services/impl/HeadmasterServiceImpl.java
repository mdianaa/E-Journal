package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.example.ejournal.repositories.HeadmasterRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.services.HeadmasterService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
public class HeadmasterServiceImpl implements HeadmasterService {

    private final HeadmasterRepository headmasterRepository;
    private final SchoolRepository schoolRepository;
    private final ModelMapper mapper;

    public HeadmasterServiceImpl(HeadmasterRepository headmasterRepository, SchoolRepository schoolRepository, ModelMapper mapper) {
        this.headmasterRepository = headmasterRepository;
        this.schoolRepository = schoolRepository;
        this.mapper = mapper;
    }

    @Override
    public HeadmasterDtoRequest createHeadmaster(HeadmasterDtoRequest headmasterDto, SchoolDtoRequest schoolDto) {
        // check whether the headmaster exists already

        // check whether the school has already a headmaster
        School school = schoolRepository.findByName(schoolDto.getName()).get();
        if (school.getHeadmaster() != null) {
            // throw exception
        }

        // register headmaster
        Headmaster headmaster = mapper.map(headmasterDto, Headmaster.class);
        headmaster.setSchool(school);

        // persist to db
        headmasterRepository.save(headmaster);

        // return dto
        return mapper.map(headmaster, HeadmasterDtoRequest.class);
    }

    @Override
    public HeadmasterDtoResponse viewHeadmaster(long headmasterId) {
        Headmaster headmaster = headmasterRepository.findById(headmasterId).get();

        return mapper.map(headmaster, HeadmasterDtoResponse.class);
    }

    @Override
    public HeadmasterDtoRequest editHeadmaster(long headmasterId, HeadmasterDtoRequest headmasterDto) {
        if (headmasterRepository.findById(headmasterId).isPresent()) {
            Headmaster headmaster = headmasterRepository.findById(headmasterId).get();

            // change headmaster entity
            mapper.map(headmasterDto, headmaster);

            // persist to db
            headmasterRepository.save(headmaster);

            // return dto
            return headmasterDto;
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