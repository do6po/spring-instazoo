package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.entities.enums.ERole;
import com.example.demo.exceptions.UserExistsException;
import com.example.demo.payload.requests.SignupRequest;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(SignupRequest userIn) throws UserExistsException {
        var user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());

            return userRepository.save(user);
        } catch (Throwable t) {
            LOG.error("Error during registration. {}", t.getMessage());

            throw new UserExistsException(String.format("The user %s already exists!", user.getEmail()));
        }
    }
}
