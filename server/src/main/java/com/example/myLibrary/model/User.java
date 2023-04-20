package com.example.myLibrary.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reader")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Setter @Getter
    private String description;
    @Setter @Getter
    private String name;
    @Setter @Getter
    private byte[] image;

    @OneToOne
    @JsonBackReference
    @Getter @Setter
    private Login login;

    @JsonBackReference
    @Getter @Setter
    @OneToMany(mappedBy = "user")
    private Set<UserBook> books;

}
