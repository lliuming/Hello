package com.example.ioc.dao;

import com.example.ioc.dao.IndexDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
@Scope("prototype")
public class IndexDaoImpl implements IndexDao {

    public IndexDaoImpl() {
        System.out.println("构造方法执行");
    }

    @Override
    public void test() {
        System.out.println("this is test methodImpl");
    }



    @PostConstruct
    public void init() {
        System.out.println("init");
    }

}
