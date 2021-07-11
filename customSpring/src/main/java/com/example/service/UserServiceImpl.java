package com.example.service;

import com.example.dao.UserDao;

public class UserServiceImpl implements UserService {

    UserDao userDao;

//    public UserServiceImpl(UserDao userDao) {
//        this.userDao = userDao;
//    }

    @Override
    public String query() {
        userDao.query();
        return "service query impl";
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
