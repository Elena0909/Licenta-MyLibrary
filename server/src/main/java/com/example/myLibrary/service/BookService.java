package com.example.myLibrary.service;

import com.example.myLibrary.mapper.BookMapper;
import com.example.myLibrary.model.*;
import com.example.myLibrary.model.dto.BookDTO;
import com.example.myLibrary.repository.*;
import com.example.myLibrary.utils.Utils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreRepository genreRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final SeriesRepository seriesRepository;

    public BookService(BookRepository bookRepository, BookMapper bookMapper, GenreRepository genreRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository, SeriesRepository seriesRepository) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.genreRepository = genreRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.seriesRepository = seriesRepository;
    }

    public List<BookDTO> getByGenres(String name, int page) {
        Optional<Genre> genreOptional = genreRepository.findByName(name);
        if (genreOptional.isPresent()) {
            Pageable pageable = PageRequest.of(page, Utils.NO_BOOKS, Sort.by("id"));
            List<Book> books = bookRepository.findAllByGenres(genreOptional.get(), pageable);
            return bookMapper.convertListBook(books);
        }
        throw new RuntimeException("Genre don't found");
    }


    public List<BookDTO> getAllByCategory(String name, int page) {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            Pageable pageable = PageRequest.of(page, Utils.NO_BOOKS, Sort.by("id"));
            List<Book> books = bookRepository.findAllByCategoryName(name, pageable);
            return bookMapper.convertListBook(books);
        }
        throw new RuntimeException("Category with name" + name + " don't found");
    }


    public List<BookDTO> getAllBooksFromAuthor(Long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if(authorOptional.isEmpty())
            throw new RuntimeException("Author don't found");

        List<Book> authorBooks = bookRepository.findAllByAuthors(authorOptional.get());

        return bookMapper.convertListBook(authorBooks);
    }

    public List<BookDTO> getAllBooksBySeries(Long id) {
        Optional<Series> seriesOptional = seriesRepository.findById(id);
        if(seriesOptional.isEmpty())
            throw new RuntimeException("Series don't found");

        List<Book> books = bookRepository.findAllBySeries(seriesOptional.get(),Sort.by("volume"));
        return bookMapper.convertListBook(books);
    }

    public List<BookDTO> search(String search, int page) {
        Pageable pageable = PageRequest.of(page, Utils.NO_BOOKS, Sort.by("id"));
        List<Book> books = bookRepository.findDistinctByNameContainingOrAndAuthorsNameContainingOrSeriesNameContaining(
                search,search,search,pageable);
        return bookMapper.convertListBook(books);
    }

}
