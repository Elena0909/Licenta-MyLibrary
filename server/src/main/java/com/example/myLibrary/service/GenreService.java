package com.example.myLibrary.service;

import com.example.myLibrary.mapper.GenreMapper;
import com.example.myLibrary.model.Genre;
import com.example.myLibrary.model.dto.GenreDTO;
import com.example.myLibrary.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenreService(GenreRepository genreRepository,GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    public List<GenreDTO> getAll() {
        List<Genre> genres = genreRepository.findAll();
        List<GenreDTO> genreDTOS = new ArrayList<>();

        for (Genre genre : genres) {
            genreDTOS.add(genreMapper.convertToGenreDTO(genre));
        }
        return genreDTOS;
    }

}
