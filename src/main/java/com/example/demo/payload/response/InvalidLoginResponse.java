package com.example.demo.payload.response;

import com.google.gson.Gson;
import lombok.Getter;

@Getter
public class InvalidLoginResponse {
    private final String username;
    private final String password;

    public InvalidLoginResponse() {
        username = "Invalid Username";
        password = "Invalid password";
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
