package com.example.demo.repositories;

import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.exceptions.PostNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByUserOrderByCreatedAtDesc(User user);

    List<Post> findAllByOrderByCreatedAtDesc();

    Optional<Post> findPostByIdAndUser(Long id, User user);

    default Post findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new PostNotFoundException("Post not found!"));
    }
}
