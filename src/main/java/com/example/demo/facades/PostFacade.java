package com.example.demo.facades;

import com.example.demo.dto.PostDTO;
import com.example.demo.entities.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    public PostDTO postToPostDTO(Post post) {
        var dto = new PostDTO();

        dto.setId(post.getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setCaption(post.getCaption());
        dto.setLikes(post.getLikes());
        dto.setUserLiked(post.getLinkedUsers());
        dto.setLocation(post.getLocation());
        dto.setTitle(post.getTitle());

        return dto;
    }
}
