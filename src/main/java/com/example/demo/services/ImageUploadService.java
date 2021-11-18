package com.example.demo.services;

import com.example.demo.repositories.ConcretePostRepository;
import com.example.demo.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {
    public static final Logger LOG = LoggerFactory.getLogger(ImageUploadService.class);

    private final ImageRepository imageRepository;
    private final UserService userService;
    private final ConcretePostRepository postRepository;

    public ImageUploadService(
            ImageRepository imageRepository,
            UserService userService,
            ConcretePostRepository postRepository
    ) {
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    private static byte[] decompressBytes(byte[] data) {
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
            LOG.error("Cannot decompress Bytes");
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
            LOG.error("Cannot compress Bytes");
        }

        byte[] bytes = outputStream.toByteArray();
        System.out.println("Compressed Image Byte Size - " + bytes.length);

        return bytes;
    }

    public <T> Collector<T, ?, T> toStringPostCollection() {
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
