package com.example.myLibrary.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "format_book")
public class FormatBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    @Setter
    private String name;

    @JsonBackReference
    @Getter @Setter
    @OneToMany(mappedBy = "formatBook")
    private Set<UserBook> books;
}