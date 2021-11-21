package com.example.demo.services;

import com.example.demo.dto.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.entities.enums.ERole;
import com.example.demo.exceptions.UserExistsException;
import com.example.demo.payload.requests.SignupRequest;
import com.example.demo.repositories.UserRepository;
import com.example.demo.traits.LogHelperTrait;
import com.example.demo.traits.PrincipalToUserTrait;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService implements PrincipalToUserTrait, LogHelperTrait {
    @Getter
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) throws UserExistsException {
        var user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            logger().info("Saving User {}", userIn.getEmail());

            return userRepository.save(user);
        } catch (Throwable t) {
            logger().error("Error during registration. {}", t.getMessage());

            throw new UserExistsException(String.format("The user %s already exists!", user.getEmail()));
        }
    }

    public User updateUser(UserDTO dto, Principal principal) {
        var user = getUserByPrincipal(principal);

        user.setName(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setUsername(dto.getUsername());
        user.setBio(dto.getBio());

        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.getByIdOrElseThrow(userId);
    }
}
