package com.example.myLibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String username;
    @Getter @Setter
    private String password;
}
