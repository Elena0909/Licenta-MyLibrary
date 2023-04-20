package com.example.mylibrary.requestModel;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    private Long id;
    private String name;
    private String photoURL;
    private  String synopsis;
    private int pages;
    private MyDate publicationDate;
    private List<Author> authors;
    private Integer volume;
    private List<Genre> genres;
    private Category category;
    private Series series;
    private Float stars;

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public Book(Long id, String name, String photoURL, String synopsis, int pages,
                MyDate publicationDate, List<Author> authors, Category category, Float stars) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
        this.synopsis = synopsis;
        this.pages = pages;
        this.publicationDate = publicationDate;
        this.authors = authors;
        this.category = category;
        this.stars = stars;
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

    public String getPhoto() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public MyDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(MyDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    public String getPhotoURL() {
        return photoURL;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }
}

