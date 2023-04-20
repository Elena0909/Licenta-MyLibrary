package com.example.mylibrary.requestModel;

import java.io.Serializable;

public class UserBook implements Serializable {
    private Long id;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @SerializedName(value = "dateStart")
//    private  LocalDate dateStart;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @SerializedName(value = "dateFinished")
//    private LocalDate dateFinished;

    public MyDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(MyDate dateStart) {
        this.dateStart = dateStart;
    }

    public MyDate getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(MyDate dateFinished) {
        this.dateFinished = dateFinished;
    }

    public Long getFormatBookId() {
        return formatBookId;
    }

    public void setFormatBookId(Long formatBookId) {
        this.formatBookId = formatBookId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public Long getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(Long bookId) {
//        this.bookId = bookId;
//    }

    private MyDate dateStart;

    private MyDate dateFinished;

    private Float stars;
    private String review;

    private Long formatBookId;
    private Long statusId;
    private User user;
//    private Long bookId;

//    private FormatBook formatBook;
//    private Status status;
//    private User user;
    private Book book;

    public UserBook()
    {

    };

//    public LocalDate getDateFinished() {
//        return dateFinished;
//    }
//
//    public void setDateFinished(LocalDate dateFinished) {
//        this.dateFinished = dateFinished;
//    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

//    public FormatBook getFormatBook() {
//        return formatBook;
//    }
//
//    public void setFormatBook(FormatBook formatBook) {
//        this.formatBook = formatBook;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public LocalDate getDateStart() {
//        return dateStart;
//    }
//
//    public void setDateStart(LocalDate dateStart) {
//        this.dateStart = dateStart;
//    }

//    public UserBook(Long id, LocalDate dateStart, LocalDate dateFinished, float stars, String review, FormatBook formatBook, Status status, User user, Book book) {
//        this.id = id;
//        this.dateStart = dateStart;
//        this.dateFinished = dateFinished;
//        this.stars = stars;
//        this.review = review;
//        this.formatBook = formatBook;
//        this.status = status;
//        this.user = user;
//        this.book = book;
//    }

    public UserBook(Long id, MyDate dateStart, MyDate dateFinished, Float  stars, String review, Long formatBook, Long status, User user, Book book) {
        this.id = id;
//        this.dateStart = dateStart;
//        this.dateFinished = dateFinished;
        this.dateStart = dateStart;
       this.dateFinished =dateFinished;
        this.stars = stars;
        this.review = review;
        this.formatBookId = formatBook;
        this.statusId = status;
        this.user = user;
        this.book = book;
    }

}
