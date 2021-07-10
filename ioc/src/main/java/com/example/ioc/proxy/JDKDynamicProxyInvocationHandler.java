package com.example.ioc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk动态代理调用处理程序
 */
public class JDKDynamicProxyInvocationHandler implements InvocationHandler {

//    目标对象
    private Object target;

    public JDKDynamicProxyInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     *
     * @param proxy 代理对象
     * @param method 目标对象的方法
     * @param args 目标对象的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("vvv");
        method.invoke(target, args);
        return null;
    }
}
