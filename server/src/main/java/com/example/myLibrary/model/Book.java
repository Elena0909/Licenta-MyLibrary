package com.example.myLibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.Nullable;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String photoURL;
    @Getter @Setter
    private  String synopsis;
    @Getter @Setter
    private int pages;
    @Getter @Setter
    private LocalDate publicationDate;
    @Getter @Setter
    @Nullable
    private Integer volume;
    @ManyToMany
    @Getter @Setter
    private Set<Author> authors;
    @ManyToMany
    @Getter @Setter
    private Set<Genre> genres;
    @ManyToOne
    @Getter
    @Setter
    private Category category;

    @Getter
    @Setter
    private Float stars;

    @ManyToOne
    @Getter @Setter
    private Series series;

    @JsonBackReference
    @Getter @Setter
    @OneToMany(mappedBy = "book")
    private Set<UserBook> userBooks;
}
