package com.example.myLibrary.controller;


import com.example.myLibrary.model.dto.UserBookDTO;
import com.example.myLibrary.service.UserBookService;
import com.example.myLibrary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/userBook")
public class UserBookController {
    private final UserBookService userBookService;
    private final UserService userService;

    public UserBookController(UserBookService userBookService, UserService userService) {
        this.userBookService = userBookService;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = {"/change"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody UserBookDTO userBookDTO, Principal principal) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.create(userId, userBookDTO));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("getFilteredBooks/{idStatus}/{page}")
    public ResponseEntity<?> getFilteredBooks(Principal principal, @PathVariable("idStatus") Long idStatus, @PathVariable("page") int page) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.getFilteredBooks(userId, idStatus, page));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("getUserBooks/{idUser}/{idStatus}/{page}")
    public ResponseEntity<?> getUserBooks(@PathVariable("idUser") Long idUser, @PathVariable("idStatus") Long idStatus, @PathVariable("page") int page) {
        try {
            return ResponseEntity.ok(userBookService.getFilteredBooks(idUser, idStatus, page));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/currentStatus/{idBook}")
    public ResponseEntity<?> getCurrentStatus(Principal principal, @PathVariable("idBook") Long idBook) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.currentStatus(userId, idBook));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/checkExist/{idBook}")
    public ResponseEntity<?> checkExist(Principal principal, @PathVariable("idBook") Long idBook) {
        Long userId = userService.getUserId(principal.getName());
        return ResponseEntity.ok(userBookService.checkExist(userId, idBook));
    }

    @GetMapping("reviewsBook/{idBook}/{page}")
    public ResponseEntity<?> reviewsBook(@PathVariable("idBook") Long idBook, @PathVariable int page) {
        try {
            return ResponseEntity.ok(userBookService.reviewsBook(idBook, page));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


}
