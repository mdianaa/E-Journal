package org.example.ejournal.services;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.models.Headmaster;

public interface HeadmasterService {

    HeadmasterDtoRequest createHeadmaster(HeadmasterDtoRequest headmasterDto, SchoolDtoRequest schoolDto);

    Headmaster viewHeadmaster(long headmasterId);

    HeadmasterDtoRequest editHeadmaster(long headmasterId, HeadmasterDtoRequest headmasterDto);

    void deleteHeadmaster(long headmasterId);
}
