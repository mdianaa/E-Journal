package org.example.ejournal.services;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;

public interface HeadmasterService {

    HeadmasterDtoRequest createHeadmaster(HeadmasterDtoRequest headmasterDto, SchoolDtoRequest schoolDto);

    HeadmasterDtoResponse viewHeadmaster(long schoolId);

    HeadmasterDtoRequest editHeadmaster(long headmasterId, HeadmasterDtoRequest headmasterDto);

    void deleteHeadmaster(long headmasterId);
}
