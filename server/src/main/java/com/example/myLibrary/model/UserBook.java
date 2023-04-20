package com.example.myLibrary.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_book")
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private LocalDate dateStart;
    @Getter @Setter
    private LocalDate dateFinished;
    @Getter @Setter
    private Float stars;
    @Getter @Setter
    private String review;

    @ManyToOne
    @Getter @Setter
    private FormatBook formatBook;

    @ManyToOne
    @Getter @Setter
    private Status status;

    @ManyToOne
    @Getter @Setter
    private User user;

    @ManyToOne
    @Getter @Setter
    private Book book;
}