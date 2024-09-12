package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.request.HeadmasterDtoRequest;
import org.example.ejournal.dtos.request.SchoolDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.HeadmasterDtoResponse;

import java.util.Set;

public interface HeadmasterService {
	HeadmasterDtoResponse  createHeadmaster(HeadmasterDtoRequest headmasterDto);
	
	HeadmasterDtoResponse viewHeadmaster(long schoolId);
	
	Set<HeadmasterDtoResponse> viewAllHeadmastersInSchool(long schoolId);
	
	Set<HeadmasterDtoResponse> viewAllHeadmasters();
	
	HeadmasterDtoResponse editHeadmaster(long headmasterId, HeadmasterDtoRequest headmasterDto);

    void deleteHeadmaster(long headmasterId);
}
