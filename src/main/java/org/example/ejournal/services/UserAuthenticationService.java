package org.example.ejournal.services;

import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.UserDtoResponse;
import org.example.ejournal.enums.RoleType;

public interface UserAuthenticationService {

    // само ако е admin
    UserDtoResponse changeUserRole(long userId, RoleType roleType);

    void register(AdminRegisterDtoRequest user);

    boolean login(String username, String password);
}
