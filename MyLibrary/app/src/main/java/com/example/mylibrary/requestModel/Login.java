package com.example.mylibrary.requestModel;

public class Login {
    public Login(String username, String password) {
        this.id=null;
        this.username = username;
        this.password = password;
    }

    private Long id;
    private String username;
    private String password;

    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
