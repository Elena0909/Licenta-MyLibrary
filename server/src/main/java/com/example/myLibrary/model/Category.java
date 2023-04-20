package com.example.myLibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    @Setter
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    @Getter
    @Setter
    private Set<Book> books;

    @Getter
    @Setter
    private String photoURL;
}
