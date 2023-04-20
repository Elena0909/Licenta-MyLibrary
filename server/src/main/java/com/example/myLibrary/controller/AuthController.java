package com.example.myLibrary.controller;


import com.example.myLibrary.model.dto.LoginDTO;
import com.example.myLibrary.security.JwtTokenProvider;
import com.example.myLibrary.service.LoginService;
import com.example.myLibrary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    public AuthController(LoginService loginService, UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.loginService = loginService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok(loginService.register(loginDTO));
        } catch (RuntimeException exception) {
            switch (exception.getMessage()) {
                case "Username can not be null!":
                case "Username already exists!":
                case "Password can not be null!":
                case "Choose a strong password, min 6 characters": {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
                }
                default: {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
                }
            }
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok(loginService.login(loginDTO));
        } catch (RuntimeException exception) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }


}