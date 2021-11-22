package com.example.demo.services;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entities.Comment;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.traits.PrincipalToUserTrait;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements PrincipalToUserTrait {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Getter
    private final UserRepository userRepository;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment save(Long id, CommentDTO dto, Principal principal) {
        var user = getUserByPrincipal(principal);
        var post = postRepository.findByIdOrElseThrow(id);

        var comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(dto.getMessage());

        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long id) {
        var post = postRepository.findByIdOrElseThrow(id);

        return post.getComments();
    }

    public void delete(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        comment.ifPresent(commentRepository::delete);
    }
}
