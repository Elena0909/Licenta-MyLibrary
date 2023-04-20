package com.example.myLibrary.repository;

import com.example.myLibrary.model.Author;
import com.example.myLibrary.model.Book;
import com.example.myLibrary.model.Genre;
import com.example.myLibrary.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findDistinctByNameContainingOrAndAuthorsNameContainingOrSeriesNameContaining(String name, String authorsName, String seriesName, Pageable pageable);

    List<Book> findAllByGenres(Genre genre, Pageable pageable);

    List<Book> findAllByCategoryName(String name,Pageable pageable);

    List<Book> findAllByAuthors(Author author);

    List<Book> findAllBySeries(Series series, Sort sort);
}