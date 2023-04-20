package com.example.myLibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    @Getter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private char sex;
    @Getter @Setter
    private String photo;

    @ManyToOne
    @Getter @Setter
    private NationalityDTO nationality;
}
