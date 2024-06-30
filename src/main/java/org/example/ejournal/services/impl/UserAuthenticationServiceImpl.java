package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.entities.User;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.repositories.UserRepository;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserAuthenticationServiceImpl(UserAuthenticationRepository userAuthenticationRepository, UserRepository userRepository, ModelMapper mapper) {
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserRegisterDtoRequest changeUserRole(long userId, RoleType role) {
        if (userAuthenticationRepository.findById(userId).isPresent()) {
            UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId).get();

            // set role
            userAuthentication.setRole(role);

            userAuthenticationRepository.save(userAuthentication);
        }
        return null;
    }

    @Override
    public void register(UserRegisterDtoRequest userDto) {
        if (userAuthenticationRepository.findByUsername(userDto.getUsername()).isEmpty()) {
            User user = mapper.map(userDto, User.class);
            UserAuthentication userAuthentication = mapper.map(userDto, UserAuthentication.class);

            user.setUserAuthentication(userAuthentication);

            userRepository.save(user);
        }
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
