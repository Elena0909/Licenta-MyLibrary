package com.example.myLibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class NationalityDTO {

    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter @Setter
    private Double latitude;

    @Getter @Setter
    private Double longitude;

}
