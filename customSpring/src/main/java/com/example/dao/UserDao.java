package com.example.dao;

public interface UserDao {

    default public String query() {
        return "default";
    }

}
