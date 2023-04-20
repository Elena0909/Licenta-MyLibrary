package com.example.myLibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String name;
}
