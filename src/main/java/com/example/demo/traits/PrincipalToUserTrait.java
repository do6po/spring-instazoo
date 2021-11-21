package com.example.demo.traits;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public interface PrincipalToUserTrait {
    UserRepository getUserRepository();

    default User getUserByPrincipal(Principal principal) {
        String username = principal.getName();

        return getUserRepository()
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                String.format("User with username %s - not found!", username)
                        )
                );
    }
}
