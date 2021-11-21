package com.example.demo.facades;

import com.example.demo.dto.CommentDTO;
import com.example.demo.entities.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    public CommentDTO commentToCommentDTO(Comment comment) {
        var dto = new CommentDTO();

        dto.setId(comment.getId());
        dto.setUsername(comment.getUsername());
        dto.setMessage(comment.getMessage());

        return dto;
    }
}
