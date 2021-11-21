package com.example.demo.repositories;

import com.example.demo.entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    default User getByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
