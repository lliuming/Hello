package com.example.ioc.proxy;

import com.example.ioc.dao.IndexDao;
import com.example.ioc.dao.IndexDaoImpl;
import org.springframework.cglib.proxy.Proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class ProxyUtil {

//    换行符
    private static final String LINE = "\r\n";

//    缩进符tab
    private static final String TAB = "\t";
    private static Object proxy;

    /**
     * content 内容
     * java文件
     * class文件
     * new出实例
     * @return
     */
    public static Object newInstance(Object target) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
        Class<?> clazz = target.getClass().getInterfaces()[0];
        String simpleName = clazz.getSimpleName();
        String packageName = "package com.example.ioc.proxy;" + LINE;
        String importContent = "import " + clazz.getName() + ";" + LINE
                + "import java.lang.reflect.Method;" + LINE;
        String content = "";
        String clazzFirstLineContent = "public class $Proxy implements " + simpleName + " {" + LINE;
        String fieldContent = TAB + "private " + simpleName + " target;" + LINE;
        String constructContent = TAB + String.format("public $Proxy(%s target) {", simpleName) + LINE
                + TAB + TAB + "this.target = target;" + LINE
                + TAB + "}" + LINE;
        Method[] methods = clazz.getDeclaredMethods();
        String allMethodContent = "";
        for (Method method : methods) {
            String methodReturnType = method.getReturnType().getSimpleName();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            String methodContent = TAB + "public " + methodReturnType + " " + methodName + "(";
            String paramContent = "";
            String paramNameContent = "";
            String paramTypeClassStr = "";
            for (int i = 0; i < parameterTypes.length; i++) {
                String paramType = parameterTypes[i].getSimpleName();
                paramTypeClassStr = paramType + ".class";
                paramContent = paramType + " arg" + i;
                paramNameContent = "arg" + i;
                if (i != (parameterTypes.length - 1)) {
                    paramContent += ", ";
                    paramNameContent += ", ";
                    paramTypeClassStr += ", ";
                }
            }
            methodContent += paramContent + ") {" + LINE
                    + TAB + TAB + "try {" + LINE
                    + TAB + TAB + TAB + "Class<?>[] parameterTypes = {" + paramTypeClassStr + "};" + LINE
                    + TAB + TAB + TAB + "Method method = target.getClass().getMethod(\"" + methodName + "\", parameterTypes);" + LINE
                    + TAB + TAB + TAB + "method.invoke(target);" + LINE
                    + TAB + TAB + "}" +  " catch (Exception e) {" + LINE
                    + TAB + TAB + "}" + LINE
//                    + TAB + TAB + "target." + methodName + "(" + paramNameContent + ");" + LINE
            + TAB + "}" + LINE;
            allMethodContent += methodContent;
        }
        content = packageName + importContent + clazzFirstLineContent + fieldContent + constructContent + allMethodContent + LINE + "}";
        File file = new File("ioc\\src\\main\\java\\com\\example\\ioc\\proxy\\$Proxy.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
//        URL url = new URI("file:/D:/Users/l1093/git/Hello/ioc/target/classes/com/example/ioc/proxy").toURL();
//        URL[] urls = {url};
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?> loadClass = classLoader.loadClass("com.example.ioc.proxy.$Proxy");
//        URLClassLoader urlClassLoader = new URLClassLoader(urls);
//        Class<?> loadClass = urlClassLoader.loadClass("com.example.ioc.proxy.$Proxy");
        Constructor<?> constructor = loadClass.getConstructor(clazz);
        ProxyUtil.proxy = constructor.newInstance(target);

        return proxy;
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
        IndexDao indexDao = (IndexDao) ProxyUtil.newInstance(new IndexDaoImpl());
        indexDao.test();
    }

}
