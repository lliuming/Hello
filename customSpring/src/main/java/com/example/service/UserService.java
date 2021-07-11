package com.example.service;

public interface UserService {

    default public String query() {
        return "default service query";
    }

}
