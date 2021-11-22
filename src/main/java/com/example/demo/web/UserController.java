package com.example.demo.web;

import com.example.demo.dto.UserDTO;
import com.example.demo.facades.UserFacade;
import com.example.demo.services.UserService;
import com.example.demo.validators.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController extends BaseController {
    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
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
    public ResponseEntity<Object> getCurrentUser(Principal principal) {
        var user = userService.getUserByPrincipal(principal);

        return response(userFacade.userToUserDTO(user));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserProfile(@PathVariable("userId") String userId) {
        var user = userService.getUserById(Long.parseLong(userId));

        return response(userFacade.userToUserDTO(user));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(
            @Valid @RequestBody UserDTO dto,
            BindingResult result,
            Principal principal
    ) {
        ResponseEntity<Object> error = responseErrorValidation.mapValidationService(result);

        if (!ObjectUtils.isEmpty(error)) {
            return error;
        }

        var updatedUser = userService.updateUser(dto, principal);

        return response(userFacade.userToUserDTO(updatedUser));
    }
}
