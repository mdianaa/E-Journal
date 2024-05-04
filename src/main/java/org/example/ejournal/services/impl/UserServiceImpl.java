package org.example.ejournal.services.impl;

import org.example.ejournal.dtos.request.UserRegisterDtoRequest;
import org.example.ejournal.enums.RoleType;
import org.example.ejournal.models.User;
import org.example.ejournal.repositories.UserRepository;
import org.example.ejournal.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserRegisterDtoRequest changeUserRole(long userId, RoleType role) {
        if (userRepository.findById(userId).isPresent()) {
            User user = userRepository.findById(userId).get();

            // set role
            user.setRole(role);

            // persist to db
            userRepository.save(user);

            // return dto
            mapper.map(user, UserRegisterDtoRequest.class);
        }

        return null;
    }

    @Override
    public void register(UserRegisterDtoRequest userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isEmpty()) {
            User user = mapper.map(userDto, User.class);

            userRepository.save(user);
        }
    }

    @Override
    public boolean login(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            User user = userRepository.findByUsername(username).get();

            if (!password.equals(user.getPassword())) {
                return false;
            }

            return true;
        }
        return false;
    }
}
