package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Genre;
import com.example.myLibrary.model.dto.GenreDTO;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {

    public GenreDTO convertToGenreDTO(Genre genre){
        return new GenreDTO(genre.getId(),genre.getName());
    }


}
