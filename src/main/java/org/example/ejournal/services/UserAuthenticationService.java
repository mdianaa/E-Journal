package org.example.ejournal.services;

import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.enums.RoleType;

public interface UserAuthenticationService {

    // само ако е admin
    UserRegisterDtoRequest changeUserRole(long userId, RoleType roleType);

    void register(UserRegisterDtoRequest user);

    boolean login(String username, String password);
}
