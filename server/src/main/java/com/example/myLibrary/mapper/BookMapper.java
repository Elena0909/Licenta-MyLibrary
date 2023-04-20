package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Book;
import com.example.myLibrary.model.Author;
import com.example.myLibrary.model.Genre;
import com.example.myLibrary.model.UserBook;
import com.example.myLibrary.model.dto.*;
import com.example.myLibrary.repository.UserBookRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {

    private final AuthorMapper authorMapper;
    private final GenreMapper genreMapper;
    private final CategoryMapper categoryMapper;
    private final SeriesMapper seriesMapper;
    private final UserBookRepository userBookRepository;

    public BookMapper(AuthorMapper authorMapper, GenreMapper genreMapper, CategoryMapper categoryMapper, SeriesMapper seriesMapper, UserBookRepository userBookRepository) {
        this.authorMapper = authorMapper;
        this.genreMapper = genreMapper;
        this.categoryMapper = categoryMapper;
        this.seriesMapper = seriesMapper;
        this.userBookRepository = userBookRepository;
    }

    public BookDTO convertToBookDTO(Book book) {

        List<AuthorDTO> authorDTOS = new ArrayList<>();
        List<GenreDTO> genreDTOS = new ArrayList<>();

        for (Author author : book.getAuthors()
        ) {
            authorDTOS.add(authorMapper.convertToAuthorDTO(author));
        }

        for (Genre genre : book.getGenres()
        ) {
            genreDTOS.add(genreMapper.convertToGenreDTO(genre));
        }

        CategoryDTO categoryDTO = categoryMapper.convertToCategoryDTO(book.getCategory());

        SeriesDTO seriesDTO = seriesMapper.convertToSeriesDTO(book.getSeries());

        MyDate publicationDate = new MyDate(book.getPublicationDate());

        Float stars;
        if(book.getStars()!=null)
            stars = book.getStars();
        else
            stars = 4f;

        return new BookDTO(book.getId(), book.getName(), book.getPhotoURL(),
                book.getSynopsis(), book.getPages(), publicationDate, book.getVolume(),
                authorDTOS, genreDTOS, categoryDTO, seriesDTO,stars);
    }

    public List<BookDTO> convertListBook(List<Book> books) {
        List<BookDTO> bookDTOS = new ArrayList<>();

        for (Book book : books
        ) {
            bookDTOS.add(convertToBookDTO(book));
        }

        return bookDTOS;
    }
}
