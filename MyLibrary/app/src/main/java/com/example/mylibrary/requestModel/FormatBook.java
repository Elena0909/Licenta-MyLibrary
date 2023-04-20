package com.example.mylibrary.requestModel;

import java.io.Serializable;

public class FormatBook implements Serializable {
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

    public FormatBook(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;
}
