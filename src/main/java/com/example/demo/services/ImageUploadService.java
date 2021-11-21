package com.example.demo.services;

import com.example.demo.entities.ImageModel;
import com.example.demo.exceptions.ImageNotFoundException;
import com.example.demo.repositories.ImageRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.traits.LogHelperTrait;
import com.example.demo.traits.PrincipalToUserTrait;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService
        implements
        LogHelperTrait,
        PrincipalToUserTrait {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;

    @Getter
    private final UserRepository userRepository;

    public ImageUploadService(
            ImageRepository imageRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        var user = getUserByPrincipal(principal);
        logger().info("Uploading image profile to User {}", user.getUsername());

        var image = imageRepository.findByUserId(user.getId()).orElse(null);

        if (!ObjectUtils.isEmpty(image)) {
            imageRepository.delete(image);
        }

        var imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());

        return imageRepository.save(imageModel);
    }

    public ImageModel uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        var user = getUserByPrincipal(principal);
        var post = user.getPosts()
                .stream()
                .filter(p -> p.getId().equals(postId))
                .collect(toStringPostCollector());

        var imageModel = new ImageModel();
        imageModel.setPostId(post.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getName());

        logger().info("Uploading image to Post {}", post.getId());

        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToPost(Long postId) {
        ImageModel imageModel = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to post"));

        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    public ImageModel getImageToUser(Principal principal) {
        var user = getUserByPrincipal(principal);

        var imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }

        return imageModel;
    }

    private byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }

            outputStream.close();
        } catch (Throwable e) {
            logger().error("Cannot decompress Bytes");
        }

        return outputStream.toByteArray();
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        try {
            outputStream.close();
        } catch (Throwable e) {
            logger().error("Cannot compress Bytes");
        }

        byte[] bytes = outputStream.toByteArray();
        System.out.println("Compressed Image Byte Size - " + bytes.length);

        return bytes;
    }

    public <T> Collector<T, ?, T> toStringPostCollector() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }

                    return list.get(0);
                }
        );
    }
}
