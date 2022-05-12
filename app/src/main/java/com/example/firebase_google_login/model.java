package com.example.firebase_google_login;

public class model {

    String email,username,image;

    model(){}

    public model(String email, String username,String image) {
        this.email = email;
        this.username = username;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
