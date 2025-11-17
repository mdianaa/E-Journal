package org.example.ejournal.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;
import org.example.ejournal.entities.Headmaster;
import org.example.ejournal.entities.School;
import org.example.ejournal.entities.User;
import org.example.ejournal.repositories.HeadmasterRepository;
import org.example.ejournal.repositories.SchoolRepository;
import org.example.ejournal.repositories.UserRepository;
import org.example.ejournal.services.HeadmasterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HeadmasterServiceImpl implements HeadmasterService {

    private final HeadmasterRepository headmasterRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public HeadmasterDtoResponse viewHeadmaster(long schoolId) {
        schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School with id " + schoolId + " not found"));

        Headmaster hm = headmasterRepository.findBySchool_Id(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("No headmaster assigned for school id " + schoolId));

        return toDto(hm);
    }

    @Override
    @Transactional
    public Set<HeadmasterDtoResponse> viewAllHeadmasters() {
        var list = headmasterRepository.findAll();
        var out = new LinkedHashSet<HeadmasterDtoResponse>(Math.max(16, list.size()));
        list.forEach(h -> out.add(toDto(h)));
        return out;
    }

    @Override
    @Transactional
    public void deleteHeadmaster(long headmasterId) {
        Headmaster hm = headmasterRepository.findById(headmasterId)
                .orElseThrow(() -> new IllegalArgumentException("Headmaster with id " + headmasterId + " not found"));

        userRepository.delete(hm.getUser());
        headmasterRepository.delete(hm);

    }

    private HeadmasterDtoResponse toDto(Headmaster h) {
        User u = h.getUser();
        School s = h.getSchool();

        HeadmasterDtoResponse dto = new HeadmasterDtoResponse();
        dto.setId(h.getId());
        dto.setUserId(u != null ? u.getId() : null);
        dto.setFullName(u != null ? u.getFirstName() + " " + u.getLastName() : null);
        dto.setEmail(u != null ? u.getEmail() : null);
        dto.setPhoneNumber(u != null ? u.getPhoneNumber() : null);
        dto.setSchoolId(s != null ? s.getId() : null);
        dto.setSchoolName(s != null ? s.getName() : null);
        return dto;
    }
}