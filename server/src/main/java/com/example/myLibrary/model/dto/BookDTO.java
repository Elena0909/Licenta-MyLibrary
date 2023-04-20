package com.example.myLibrary.model.dto;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @Getter
    private Long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String photoURL;
    @Getter
    @Setter
    private String synopsis;
    @Getter
    @Setter
    private int pages;
    @Getter
    @Setter
    private MyDate publicationDate;
    @Getter
    @Setter
    @Nullable
    private Integer volume;
    @Getter
    @Setter
    private List<AuthorDTO> authors;
    @Getter
    @Setter
    private List<GenreDTO> genres;
    @Getter
    @Setter
    private CategoryDTO category;
    @Getter
    @Setter
    private SeriesDTO series;
    @Getter
    @Setter
    private Float stars;
}
