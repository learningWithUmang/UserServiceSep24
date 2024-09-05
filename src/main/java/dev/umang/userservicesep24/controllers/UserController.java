package dev.umang.userservicesep24.controllers;

import dev.umang.userservicesep24.dtos.ResponseStatus;
import dev.umang.userservicesep24.dtos.SignupRequestDTO;
import dev.umang.userservicesep24.dtos.SignupResponseDTO;
import dev.umang.userservicesep24.models.User;
import dev.umang.userservicesep24.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users") // localhost:8080/users/...
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    /*
    Take the requests from the users
    1. signup
    2. login
    3. logout
    4. validate token
     */
    @PostMapping("/signup")
    public SignupResponseDTO signup(@RequestBody SignupRequestDTO signupRequestDTO){ //REST method
        String name = signupRequestDTO.getName();
        String email = signupRequestDTO.getEmail();
        String password = signupRequestDTO.getPassword();

        //call the user service to register the user and return the user back to the client
        //return user response dto back to the client

        SignupResponseDTO signupResponseDTO = new SignupResponseDTO();
        try {
            User user = userService.signup(name, email, password);
            signupResponseDTO.setEmail(user.getEmail());
            signupResponseDTO.setName(user.getName());
            signupResponseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception ex){
            signupResponseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }

        return signupResponseDTO;
    }




}
