package com.example.demo.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class DatabaseImpl implements DatabaseRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveUser(User user) {
         jdbcTemplate.update(
                "insert into Users (id, name, email) values(?,?,?)",
                user.id, user.name, user.email);
    }

    @Override
    public void saveImage(Image image) {
         jdbcTemplate.update(
                "insert into Images (user_id, image) values(?,?)",
                image.userId, image.image);
    }

    @Override
    public User getUser(String userId) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        User target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new User(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ), new Object[] { userId });

        return target;
    }

    @Override
    public Image getImage(String userId) {
        String sql = "SELECT * FROM Images WHERE user_id = ?";
        Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Image(
                        rs.getString("user_id"),
                        rs.getBytes("image")
                ),new Object[]{userId});

        return target;
    }
}
