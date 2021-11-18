package com.example.demo.services;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entities.Comment;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.ConcretePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ConcretePostRepository postRepository;
    private final UserService userService;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            ConcretePostRepository postRepository,
            UserService userService
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Comment save(Long id, CommentDTO dto, Principal principal) {
        var user = userService.getUserFromPrincipal(principal);
        var post = postRepository.findByIdOrThrow(id);

        var comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(dto.getMessage());

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long id) {
        var post = postRepository.findByIdOrThrow(id);

        return post.getComments();
    }

    public void delete(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        comment.ifPresent(commentRepository::delete);
    }
}
