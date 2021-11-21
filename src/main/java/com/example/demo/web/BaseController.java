package com.example.demo.web;

import com.example.demo.payload.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected ResponseEntity<Object> response(Object o, HttpStatus status) {
        return new ResponseEntity<>(o, status);
    }

    protected ResponseEntity<Object> response(Object o) {
        return response(o, HttpStatus.OK);
    }

    protected ResponseEntity<Object> messageResponse(String message, HttpStatus status) {
        return response(
                new MessageResponse(message),
                status
        );
    }

    protected ResponseEntity<Object> messageResponse(String message) {
        return messageResponse(message, HttpStatus.OK);
    }
}
