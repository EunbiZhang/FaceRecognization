package com.example.demo.database;

import java.sql.Timestamp;

public class Image {
    public String userId;
    public byte[] image;
    public Timestamp createdAt;

    public Image(String userId, byte[] image) {
        this.userId = userId;
        this.image = image;
    }
}
