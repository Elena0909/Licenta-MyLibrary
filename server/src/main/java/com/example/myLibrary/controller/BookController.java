package com.example.myLibrary.controller;

import com.example.myLibrary.model.Book;
import com.example.myLibrary.model.Category;
import com.example.myLibrary.model.Genre;
import com.example.myLibrary.model.Series;
import com.example.myLibrary.model.dto.AuthorDTO;
import com.example.myLibrary.model.dto.BookDTO;
import com.example.myLibrary.model.dto.CategoryDTO;
import com.example.myLibrary.model.dto.GenreDTO;
import com.example.myLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/genre/{name}/books/{page}")
    public ResponseEntity<?> getAllByGenre(@PathVariable("name") String genre, @PathVariable("page") int page) {
        try {
            return ResponseEntity.ok(bookService.getByGenres(genre, page));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/category/{name}/books/{page}")
    public ResponseEntity<?> getAllByCategory(@PathVariable("name") String name, @PathVariable("page") int page) {
        try {
            return ResponseEntity.ok(bookService.getAllByCategory(name, page));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{search}/{page}")
    public ResponseEntity<?> search(@PathVariable("search") String search, @PathVariable("page") int page) {
        try {
            return ResponseEntity.ok(bookService.search(search, page));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/author/books/{id}")
    public ResponseEntity<?> findAllByAuthorId(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(bookService.getAllBooksFromAuthor(id));
        } catch (RuntimeException e) {
            if(e.getMessage().equals("Author don't found"))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/series/books/{id}")
    public ResponseEntity<?> findAllBySeriesId(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(bookService.getAllBooksBySeries(id));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
