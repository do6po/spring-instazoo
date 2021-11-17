package com.example.demo.services;

import com.example.demo.dto.PostDTO;
import com.example.demo.entities.ImageModel;
import com.example.demo.entities.Post;
import com.example.demo.exceptions.PostNotFoundException;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            ImageRepository imageRepository,
            UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public Post createPost(PostDTO dto, Principal principal) {
        var user = userService.getUserFromPrincipal(principal);
        var post = new Post();

        post.setUser(user);
        post.setCaption(dto.getCaption());
        post.setLocation(dto.getLocation());
        post.setTitle(dto.getTitle());

        LOG.info("Saving post for User: {}", user.getEmail());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Post> getAllPostForUser(Principal principal) {
        var user = userService.getUserFromPrincipal(principal);

        return postRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    public Post likePost(Long id, String username) {
        var post = postRepository
                .findById(id)
                .orElseThrow(throwPostNotFound())
                .like(username);

        return postRepository.save(post);
    }

    public Post getPostById(Long id, Principal principal) {
        var user = userService.getUserFromPrincipal(principal);

        return postRepository
                .findPostByIdAndUser(id, user)
                .orElseThrow(throwPostNotFound());
    }

    public void deletePost(Long id, Principal principal) {
        var post = getPostById(id, principal);

        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());

        postRepository.delete(post);

        imageModel.ifPresent(imageRepository::delete);
    }

    private Supplier<PostNotFoundException> throwPostNotFound() {
        return () -> new PostNotFoundException("Post not found!");
    }
}
