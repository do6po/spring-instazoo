package com.example.demo.web;

import com.example.demo.dto.PostDTO;
import com.example.demo.facades.PostFacade;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.services.PostService;
import com.example.demo.validators.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
@CrossOrigin
public class PostController {
    private final PostFacade postFacade;
    private final PostService postService;
    private final ResponseErrorValidation responseErrorValidation;

    public PostController(
            PostFacade postFacade,
            PostService postService,
            ResponseErrorValidation responseErrorValidation
    ) {
        this.postFacade = postFacade;
        this.postService = postService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(
            @Valid @RequestBody PostDTO dto,
            BindingResult result,
            Principal principal
    ) {
        var error = responseErrorValidation.mapValidationService(result);

        if (!ObjectUtils.isEmpty(error)) {
            return error;
        }

        var updatedPost = postService.createPost(dto, principal);

        return new ResponseEntity<>(
                postFacade.postToPostDTO(updatedPost),
                HttpStatus.OK
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsForUser(Principal principal) {
        List<PostDTO> postDTOList = postService.getAllPostForUser(principal)
                .stream()
                .map(postFacade::postToPostDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDTO> likePost(
            @PathVariable("postId") String postId,
            @PathVariable("username") String username
    ) {
        var post = postService.likePost(Long.parseLong(postId), username);

        return new ResponseEntity<>(
                postFacade.postToPostDTO(post),
                HttpStatus.OK
        );
    }

    @PostMapping("{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(
            @PathVariable("postId") String postId,
            Principal principal
    ) {
        postService.deletePost(
                Long.parseLong(postId),
                principal
        );

        return new ResponseEntity<>(
                new MessageResponse("Post was deleted!"),
                HttpStatus.OK
        );
    }
}
