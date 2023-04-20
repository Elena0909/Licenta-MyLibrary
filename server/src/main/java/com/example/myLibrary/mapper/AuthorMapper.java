package com.example.myLibrary.mapper;

import com.example.myLibrary.model.Author;
import com.example.myLibrary.model.dto.AuthorDTO;
import com.example.myLibrary.model.dto.NationalityDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    private final NationalityMapper nationalityMapper;

    public AuthorMapper(NationalityMapper nationalityMapper) {
        this.nationalityMapper = nationalityMapper;
    }

    public AuthorDTO convertToAuthorDTO(Author author){
        NationalityDTO nationalityDTO = nationalityMapper.convertToNationalityDTO(author.getNationality());
        return new AuthorDTO(author.getId(),author.getName(), author.getDescription(),author.getSex(),author.getPhoto(),nationalityDTO);
    }

}
