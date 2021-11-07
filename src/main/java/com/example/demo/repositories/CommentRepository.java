package com.example.demo.repositories;

import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}
