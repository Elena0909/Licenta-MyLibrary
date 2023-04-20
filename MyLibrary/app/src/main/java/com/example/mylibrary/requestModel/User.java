package com.example.mylibrary.requestModel;

import java.io.Serializable;

public class User implements Serializable {

    private Long id;


    public User(Long id, String name, String description, String photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = photo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String name;
    private String description;
    private String image;

}
