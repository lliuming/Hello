package com.example.ioc.dao;

import org.springframework.stereotype.Repository;

public interface IndexDao {

    default public void test() {

    }

    default public void test(String str) {
        System.out.println(str);
    }

}
