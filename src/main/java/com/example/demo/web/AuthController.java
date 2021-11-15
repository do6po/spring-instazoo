package com.example.demo.web;

import com.example.demo.payload.requests.LoginRequest;
import com.example.demo.payload.requests.SignupRequest;
import com.example.demo.payload.response.JWTTokenResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.JWTTokenProvider;
import com.example.demo.security.SecurityConstants;
import com.example.demo.services.UserService;
import com.example.demo.validators.ResponseErrorValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    private final JWTTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;

    public AuthController(
            JWTTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager,
            ResponseErrorValidation responseErrorValidation,
            UserService userService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<Object> register(
            @Valid @RequestBody SignupRequest request,
            BindingResult result
    ) {
        var errors = responseErrorValidation.mapValidationService(result);
        if (ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        userService.create(request);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> auth(@Valid @RequestBody LoginRequest request, BindingResult result) {
        var errors = responseErrorValidation.mapValidationService(result);
        if (ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = String.format("%s %s",
                SecurityConstants.TOKEN_PREFIX,
                jwtTokenProvider.generateToken(authentication)
        );

        return ResponseEntity.ok(new JWTTokenResponse(true, jwt));
    }
}
