package com.example.demo.web;

import com.example.demo.dto.UserDTO;
import com.example.demo.facades.UserFacade;
import com.example.demo.services.UserService;
import com.example.demo.validators.ResponseErrorValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidation responseErrorValidation;

    public UserController(
            UserService userService,
            UserFacade userFacade,
            ResponseErrorValidation responseErrorValidation
    ) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        var user = userService.getUserByPrincipal(principal);

        return new ResponseEntity<>(
                userFacade.userToUserDTO(user),
                HttpStatus.OK
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable("userId") String userId) {
        var user = userService.getUserById(Long.parseLong(userId));

        return new ResponseEntity<>(
                userFacade.userToUserDTO(user),
                HttpStatus.OK
        );
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO dto, BindingResult result, Principal principal) {
        ResponseEntity<Object> error = responseErrorValidation.mapValidationService(result);

        if (!ObjectUtils.isEmpty(error)) {
            return error;
        }

        var updatedUser = userService.updateUser(dto, principal);

        return new ResponseEntity<>(
                userFacade.userToUserDTO(updatedUser),
                HttpStatus.OK
        );
    }
}
