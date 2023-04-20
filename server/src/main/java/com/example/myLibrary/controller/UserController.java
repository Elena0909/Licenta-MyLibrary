package com.example.myLibrary.controller;

import com.example.myLibrary.model.dto.LoginDTO;
import com.example.myLibrary.model.dto.UserDTO;
import com.example.myLibrary.service.LoginService;
import com.example.myLibrary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserDTO userDTO) {
        try{
            return ResponseEntity.ok(userService.update(userDTO));
        }
        catch (RuntimeException exception) {
            if (exception.getMessage().equals("User don't found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping(value = "getUserDetails")
    public ResponseEntity<?> getUserDetails(Principal principal) {
        try{
        String username = principal.getName();
        LoginDTO loginDTO = loginService.findByUsername(username);
        UserDTO userDTO = userService.findByLoginId(loginDTO.getId());
        return ResponseEntity.ok(userDTO);
        }
        catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
