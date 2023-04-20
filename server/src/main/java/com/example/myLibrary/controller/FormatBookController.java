package com.example.myLibrary.controller;

import com.example.myLibrary.service.FormatBookServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "format")
public class FormatBookController {
    private final FormatBookServer formatBookServer;

    public FormatBookController(FormatBookServer formatBookServer) {
        this.formatBookServer = formatBookServer;
    }

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        try {
            return ResponseEntity.ok(formatBookServer.getAll());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
