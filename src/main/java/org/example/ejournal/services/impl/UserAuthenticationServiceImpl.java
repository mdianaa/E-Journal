package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.AdminRegisterDtoRequest;
import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.dtos.response.UserDtoResponse;
import org.example.ejournal.entities.User;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.entities.UserAuthentication;
import org.example.ejournal.repositories.UserAuthenticationRepository;
import org.example.ejournal.repositories.UserRepository;
import org.example.ejournal.services.UserAuthenticationService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

    private final UserAuthenticationRepository userAuthenticationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationServiceImpl(UserAuthenticationRepository userAuthenticationRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userAuthenticationRepository = userAuthenticationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDtoResponse changeUserRole(long userId, RoleType role) {
        if (userAuthenticationRepository.findById(userId).isPresent()) {
            UserAuthentication userAuthentication = userAuthenticationRepository.findById(userId).get();

            // set role
            userAuthentication.setRole(role);

            userAuthenticationRepository.save(userAuthentication);
        }
        return null;
    }

    @Override
    public void register(AdminRegisterDtoRequest userDto) {
        if (userAuthenticationRepository.findByUsername(userDto.getUsername()).isEmpty()) {
            User user = new User();

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhoneNumber(userDto.getPhoneNumber());

            UserAuthentication userAuthentication = new UserAuthentication();

            userAuthentication.setUsername(userDto.getUsername());
            userAuthentication.setPassword(passwordEncoder.encode(userDto.getPassword())); // encode password
            userAuthentication.setRole(userDto.getRole());

            user.setUserAuthentication(userAuthentication);

            userAuthenticationRepository.save(userAuthentication);
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
