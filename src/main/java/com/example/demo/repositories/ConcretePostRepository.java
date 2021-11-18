package com.example.demo.repositories;

import com.example.demo.entities.Post;
import com.example.demo.exceptions.PostNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository
public abstract class ConcretePostRepository implements PostRepository {
    public Post findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new PostNotFoundException("Post not found!"));
    }
}
