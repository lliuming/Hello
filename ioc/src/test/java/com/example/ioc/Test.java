package com.example.ioc;

import com.example.ioc.annotation.TableName;
import com.example.ioc.dao.DeclareParentDao;
import com.example.ioc.dao.IndexDao;
import com.example.ioc.dao.IndexDaoImpl;
import com.example.ioc.entity.User;
import com.example.ioc.proxy.JDKDynamicProxyInvocationHandler;
import com.example.ioc.service.IndexService;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Proxy;

public class Test {

    @org.junit.Test
    public void test() {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring.xml");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Spring.class);
//        IndexService indexService = (IndexService) context.getBean("indexService");
//        indexService.service();
//        context.getBean(IndexDao.class).test("你好");
//        IndexDao bean = (IndexDao) context.getBean(DeclareParentDao.class);
//        bean.test();
        IndexDaoImpl bean = context.getBean(IndexDaoImpl.class);
        IndexDaoImpl bean2 = context.getBean(IndexDaoImpl.class);
        bean.test();
        bean2.test();
    }

    @org.junit.Test
    public void getAnnotation() {
        Class<User> userClass = User.class;
        System.out.println(userClass.isAnnotationPresent(TableName.class));;
    }

    /**
     * jdk动态代理
     */
    @org.junit.Test
    public void jdkDynamicProxy() {
        Class<?>[] classes = {IndexDao.class};
        IndexDao indexDao = (IndexDao) Proxy.newProxyInstance(getClass().getClassLoader(), classes, new JDKDynamicProxyInvocationHandler(new IndexDaoImpl()));
        indexDao.test();
    }

}
