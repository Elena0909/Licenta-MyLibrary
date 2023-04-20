package com.example.myLibrary.controller;

import com.example.myLibrary.service.UserBookService;
import com.example.myLibrary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final UserBookService userBookService;
    private final UserService userService;

    public StatsController(UserBookService userBookService, UserService userService) {
        this.userBookService = userBookService;
        this.userService = userService;
    }

    //carti citite pe an sortate dupa nr de stelute
    @GetMapping("booksAndStars/{year}")
    public ResponseEntity<?> booksAndStars(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.booksAndStars(userId, year));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("booksReadPerYear/{year}")
    public ResponseEntity<?> booksReadPerYear(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.booksDTOReadPerYear(userId, year));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/getAllBooksRead/{year}")
    public ResponseEntity<?> getAllBooksRead(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.getAllBooksReadForMap(userId, year));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    //carti citite pe fiecare luna -succesc cu map:))
    @GetMapping("booksReadPerMonth/{year}")
    public ResponseEntity<?> booksReadPerMonth(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.booksReadPerMonth(userId, year));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/pagesReadPerMonth/{year}")
    public ResponseEntity<?> pagesReadPerMonth(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.pagesReadPerMonth(userId, year));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/slowestBook/{year}")
    public ResponseEntity<?> slowestBook(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.slowestBook(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/fastestBook/{year}")
    public ResponseEntity<?> fastestBook(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.fastestBook(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/longestBook/{year}")
    public ResponseEntity<?> longestBook(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.longestBook(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/shortestBook/{year}")
    public ResponseEntity<?> shortestBook(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.shortestBook(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/yourYear/{year}")
    public ResponseEntity<?> yourYear(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.yourYear(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/booksGenre/{year}")
    public ResponseEntity<?> booksGenre(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.booksGenre(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/bookCategories/{year}")
    public ResponseEntity<?> bookCategories(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.bookCategories(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/bookFormats/{year}")
    public ResponseEntity<?> bookFormats(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.bookFormats(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/genAuthors/{year}")
    public ResponseEntity<?> genAuthors(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.genAuthors(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/favoriteAuthor/{year}")
    public ResponseEntity<?> favoriteAuthor(Principal principal, @PathVariable("year") Integer year) {
        try {
            Long userId = userService.getUserId(principal.getName());
            return ResponseEntity.ok(userBookService.favoriteAuthor(userId, year));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

}
