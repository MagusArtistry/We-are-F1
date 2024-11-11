package com.example.wearef1;

public class User {
    private String email;
    private String username;
    private String favoriteDriverId;
    public User() {
    }

    public User(String email, String username){
        this.email = email;
        this.username = username;
        this.favoriteDriverId = "";
    }

    public User(String email, String username, String favoriteDriverId){
        this.email = email;
        this.username = username;
        this.favoriteDriverId = favoriteDriverId;
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

    public String getFavoriteDriverId() {
        return favoriteDriverId;
    }
    public void setFavoriteDriverId(String favoriteDriverId) {
        this.favoriteDriverId = favoriteDriverId;
    }
}
