package com.example.demo.facades;

import com.example.demo.dto.UserDTO;
import com.example.demo.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user) {
        var dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstname(user.getName());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setBio(user.getBio());

        return dto;
    }
}
