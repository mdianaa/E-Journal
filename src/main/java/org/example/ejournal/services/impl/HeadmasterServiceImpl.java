package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.models.Headmaster;
import org.example.ejournal.models.School;
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
    public Headmaster viewHeadmaster(long headmasterId) {
        return headmasterRepository.findById(headmasterId).get();
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

        // throw exception
        return null;
    }

    @Override
    public void deleteHeadmaster(long headmasterId) {
        if (headmasterRepository.findById(headmasterId).isPresent()) {
            Headmaster headmaster = headmasterRepository.findById(headmasterId).get();

            headmaster.setSchool(null);

            headmasterRepository.delete(headmaster);
        }

        // throw exception
    }
}
