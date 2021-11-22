package com.example.demo.web;

import com.example.demo.services.ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageController extends BaseController {
    private final ImageUploadService imageUploadService;

    public ImageController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImageToUser(
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);

        return messageResponse("Image Uploaded Successfully.");
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<Object> upload(
            @PathVariable("postId") String postId,
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) throws IOException {
        imageUploadService.uploadImageToPost(file, principal, Long.parseLong(postId));

        return messageResponse("Image uploaded Successfully.");
    }

    @GetMapping("/profileImage")
    public ResponseEntity<Object> getImageForUser(Principal principal) {
        return response(
                imageUploadService.getImageToUser(principal)
        );
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Object> getImageToPost(@PathVariable("postId") String postId) {
        return response(
                imageUploadService.getImageToPost(Long.parseLong(postId))
        );
    }
}
