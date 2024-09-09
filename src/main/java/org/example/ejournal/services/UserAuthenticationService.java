package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.response.LoginResponseDto;
import org.example.ejournal.dtos.response.UserDtoResponse;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.enums.RoleType;

public interface UserAuthenticationService {

    // само ако е admin
    UserDtoResponse changeUserRole(long userId, RoleType roleType);

    UserAuthentication register(AdminRegisterDtoRequest user);

    LoginResponseDto login(String username, String password);
}
