package com.example.demo.web;

import com.example.demo.dto.CommentDTO;
import com.example.demo.facades.CommentFacade;
import com.example.demo.services.CommentService;
import com.example.demo.validators.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("api/comment")
public class CommentController extends BaseController {
    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(
            CommentService commentService,
            CommentFacade commentFacade,
            ResponseErrorValidation responseErrorValidation
    ) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @RequestMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(
            @Valid @RequestBody CommentDTO dto,
            @PathVariable("postId") String postId,
            BindingResult result,
            Principal principal
    ) {
        var errors = responseErrorValidation.mapValidationService(result);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        var createdComment = commentService.save(Long.parseLong(postId), dto, principal);

        return response(
                commentFacade.commentToCommentDTO(createdComment),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<Object> getAllCommentsToPost(
            @PathVariable("postId") String postId
    ) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());

        return response(commentDTOList);
    }

    @PostMapping("/{commentId}/delete")
    public ResponseEntity<Object> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.delete(Long.parseLong(commentId));

        return messageResponse("Comment was deleted.", HttpStatus.NO_CONTENT);
    }

}
