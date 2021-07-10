package com.example.ioc.aspect;

import com.example.ioc.dao.IndexDao;
import com.example.ioc.dao.IndexDaoImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Component
@Aspect("perthis(daoPointCutThis())")
@Scope("prototype")
public class IndexAspect {

    @DeclareParents(value = "com.example.ioc.dao.*", defaultImpl = IndexDaoImpl.class)
    public static IndexDao INDEX_DAO;

    /**
     * dao下面的任意方法
     */
    @Pointcut("execution(* com.example.ioc.dao.*.*(..))")
    public void daoPointCut() {}

    /**
     * dao下面的额所有类
     */
    @Pointcut("within(com.example.ioc.dao.*)")
    public void daoPointCutWithin() {}

    /**
     * 当前对象是indexDaoImpl触发
     */
    @Pointcut("this(com.example.ioc.dao.IndexDaoImpl)")
    public void daoPointCutThis() {}

    /**
     * 后置通知
     */
//    @After("daoPointCutWithin()")
    public void doSomethingAfter() {
        System.out.println("after");
    }

    /**
     * 前置通知
     */
    @Before("daoPointCutThis()")
    public void doAccessCheck(JoinPoint joinPoint) {
        System.out.println("before");
        System.out.println(this.hashCode());
        System.out.println(joinPoint.getSignature());
        System.out.println(joinPoint.getThis() instanceof Proxy);
    }

    /**
     * 环绕通知
     * @param proceedingJoinPoint 正在进行的连接点
     */
//    @Around("daoPointCutWithin()")
    public void doSomethingAround(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("around before");
        Object[] args = proceedingJoinPoint.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                args[i] += "word";
            }
        }
        try {
//            执行这个才会触发before通知，否则前置通知不触发
            proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
        }
        System.out.println("around");
    }

}
