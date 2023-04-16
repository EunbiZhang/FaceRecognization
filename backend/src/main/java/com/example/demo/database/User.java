package com.example.demo.database;

import java.sql.Timestamp;

public class User {
    public String id;
    public String name;
    public String email;
    public Timestamp createdAt;

    public User(String id, String name, String email) {
        this.id=id;
        this.name=name;
        this.email=email;
    }
}