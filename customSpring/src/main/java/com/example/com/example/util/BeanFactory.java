package com.example.com.example.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeanFactory {

    Map<String, Object> objectMap = new HashMap<>();

    /**
     * bean工厂信息
     * @param xml
     */
    public BeanFactory(String xml) {
        parseXml(xml);
    }

    /**
     * 解析xml
     * @param xml
     */
    public void parseXml(String xml) {
        File file = new File(this.getClass().getResource("/").getPath() + "/" + xml);
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();
            // iterate through child elements of root
            for (Iterator<Element> it = rootElement.elementIterator(); it.hasNext();) {
                Element element = it.next();
                String id = element.attributeValue("id");
                String clazzName = element.attributeValue("class");
                Class<?> aClass = Class.forName(clazzName);
                Object o = null;
//                objectMap.put(id, o);
                /**
                 * 维护依赖关系
                 * 看看是否有依赖（判断是否有属性）
                 */
                // do something
                if (!element.elementIterator("property").hasNext() && !element.elementIterator("constructor-arg").hasNext()) {
                    o = aClass.newInstance();
                }
                for (Iterator<Element> beanIt = element.elementIterator("property"); beanIt.hasNext();) {
                    o = aClass.newInstance();
                    Element next = beanIt.next();
                    String name = next.attributeValue("name");
                    String ref = next.attributeValue("ref");
                    if (ref != null) {
                        // 开始setter注入
                        Object o1 = objectMap.get(ref);
                        Field declaredField = aClass.getDeclaredField(name);
                        declaredField.setAccessible(true);
                        declaredField.set(o, o1);
                    }
                }
                for (Iterator<Element> beanIt = element.elementIterator("constructor-arg"); beanIt.hasNext();) {
                    Element next = beanIt.next();
                    String name = next.attributeValue("name");
                    String ref = next.attributeValue("ref");
                    if (ref != null) {
                        // 开始构造注入
                        Class<?> aClass1 = objectMap.get(ref).getClass();
//                        获取字段
                        Field declaredField = aClass.getDeclaredField(name);
//                        获取声明字段的类型
                        Class<?> type = declaredField.getType();
                        Constructor<?> constructor = aClass.getConstructor(type);
                        o = constructor.newInstance(objectMap.get(ref));
                    }
                }
                objectMap.put(id, o);

            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(objectMap);
    }

    /**
     * 获取bean
     * @param beanId bean唯一标识
     * @return
     */
    public Object getBean(String beanId) {
        return objectMap.get(beanId);
    }

    public static void main(String[] args) {
//        System.out.println(BeanFactory.class.getResource("/").getPath());
    }

}
