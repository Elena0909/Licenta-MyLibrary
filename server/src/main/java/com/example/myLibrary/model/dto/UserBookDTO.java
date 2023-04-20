package com.example.myLibrary.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
public class UserBookDTO {
    @Getter
    @Nullable
    private Long id;
    @Getter @Setter
    @Nullable
    private MyDate dateStart;
    @Getter @Setter
    private MyDate dateFinished;
    @Getter @Setter
    @Nullable
    private Float stars;
    @Getter @Setter
    @Nullable
    private String review;

    @Getter @Setter
    @Nullable
    private Long formatBookId;

    @Getter @Setter
    private Long statusId;

    @Getter @Setter
    private UserDTO user;

//    @Getter @Setter
//    private Long bookId;


//    @Getter @Setter
//    private FormatBookDTO formatBook;
//
//    @Getter @Setter
//    private StatusDTO status;
//
//    @Getter @Setter
//    private UserDTO user;
//
    @Getter @Setter
    private BookDTO book;

}
