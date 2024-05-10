package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.models.User;
import org.example.ejournal.models.UserAuthentication;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final ModelMapper mapper;

    public UserAuthenticationServiceImpl(UserAuthenticationRepository userAuthenticationRepository, ModelMapper mapper) {
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.mapper = mapper;
    }

    @Override
    public UserRegisterDtoRequest changeUserRole(long userId, RoleType role) {
        if (userAuthenticationRepository.findById(userId).isPresent()) {
//            User user = userAuthenticationRepository.findById(userId).get();
//
//            // set role
//            user.setRole(role);
//
//            // persist to db
//            userAuthenticationRepository.save(user);

            // return dto
//            mapper.map(user, UserRegisterDtoRequest.class);
        }

        return null;
    }

    @Override
    public void register(UserRegisterDtoRequest userDto) {
//        if (userAuthenticationRepository.findByUsername(userDto.getUsername()).isEmpty()) {
//            User user = mapper.map(userDto, User.class);
//
//            userAuthenticationRepository.save(user);
//        }
    }

    @Override
    public boolean login(String username, String password) {
        if (userAuthenticationRepository.findByUsername(username).isPresent()) {
            UserAuthentication user = userAuthenticationRepository.findByUsername(username).get();

            return password.equals(user.getPassword());
        }
        return false;
    }
}
