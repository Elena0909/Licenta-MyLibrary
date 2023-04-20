package com.example.mylibrary.requestModel;

import java.io.Serializable;

public class Category implements Serializable {
    private Long id;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(Long id, String name, String photoURL) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    private String photoURL;
}
