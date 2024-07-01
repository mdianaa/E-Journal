package org.example.ejournal.services;

import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;

public interface HeadmasterService {

    HeadmasterDtoResponse createHeadmaster(HeadmasterDtoRequest headmasterDto, SchoolDtoRequest schoolDto, UserRegisterDtoRequest userRegisterDtoRequest);

    HeadmasterDtoResponse viewHeadmaster(long schoolId);

    HeadmasterDtoResponse editHeadmaster(long headmasterId, HeadmasterDtoRequest headmasterDto);

    void deleteHeadmaster(long headmasterId);
}
