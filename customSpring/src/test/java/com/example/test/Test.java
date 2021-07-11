package com.example.test;

import com.example.com.example.util.BeanFactory;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;

public class Test {

    @org.junit.Test
    public void test() {
        BeanFactory beanFactory = new BeanFactory("spring.xml");
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.query();
    }


}
