package org.example.ejournal.services;

import org.example.ejournal.dtos.response.HeadmasterDtoResponse;

import java.util.Set;

public interface HeadmasterService {

    HeadmasterDtoResponse viewHeadmaster(long schoolId);

    Set<HeadmasterDtoResponse> viewAllHeadmasters();

    void deleteHeadmaster(long headmasterId);
}
