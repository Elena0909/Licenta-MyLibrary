package com.example.myLibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
public class SeriesDTO {
    @Getter
    private Long id;
    @Getter
    @Setter
    private String name;
}
