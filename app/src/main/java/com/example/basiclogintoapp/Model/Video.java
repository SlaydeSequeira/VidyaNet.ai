package com.example.basiclogintoapp.Model;

public class Video {
    public String UID;
    public String description;
    public String title;
    public String url;

    // Default constructor for Firebase
    public Video() {}

    // Constructor with parameters
    public Video(String UID, String description, String title, String url) {
        this.UID = UID;
        this.description = description;
        this.title = title;
        this.url = url;
    }
}

