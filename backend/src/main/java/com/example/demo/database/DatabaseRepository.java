package com.example.demo.database;

public interface DatabaseRepository {
    void saveUser(User user);
    void saveImage(Image image);
    User getUser(String userId);
    Image getImage(String userId);
}
