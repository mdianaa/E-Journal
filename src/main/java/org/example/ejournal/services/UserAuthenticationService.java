package org.example.ejournal.services;

import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.LoginResponseDto;
import org.example.ejournal.dtos.response.UserDtoResponse;
import org.example.ejournal.entities.User;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.enums.RoleType;
import org.springframework.transaction.annotation.Transactional;

public interface UserAuthenticationService {

    // само ако е admin
    UserDtoResponse changeUserRole(long userId, RoleType roleType);
    
    @Transactional
    UserAuthentication register(UserRegisterDtoRequest userDto);
    
    LoginResponseDto login(String username, String password);
    
    User getAuthenticatedUser();
}
