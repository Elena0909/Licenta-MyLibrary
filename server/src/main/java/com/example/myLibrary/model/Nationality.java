package com.example.myLibrary.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "nationality")
public class Nationality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Double latitude;

    @Getter @Setter
    private Double longitude;

    @JsonBackReference
    @Getter @Setter
    @OneToMany(mappedBy = "nationality")
    private Set<Author> authors;

}
