package utils;

import annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhuran on 2019/1/17 0017
 */
@Slf4j
public class ScanUtil {

    //类名的集合
    private static List<String> listClassName = new ArrayList<>();
    //接口和实现类集合
    private static Map<String, String> interfaceAndImplMap = new ConcurrentHashMap<>();
    //有注解实例化集合
    private static List<String> componentList = new ArrayList<>();

    /**
     * 返回 有注解的实例化顺序的链表
     */
    public static List<String> getComponentList(String packageName) {
        //获取包下所有的类
        List<String> classNameList = getClassName(packageName);
        //将扫描的接口和其实现类，使用map对应上,模仿spring接口注入，复杂的原因是java不支持从接口获取实现类
        makeInterfaceAndImplMap(classNameList);
        for (String className : classNameList) {
            //实例化每个类
            try {
                resolveComponent(className);
            } catch (ClassNotFoundException e) {
                log.error("扫描注解的时候,{}没有找到", className);
                e.printStackTrace();
            }
        }
        return componentList;
    }

    private static void resolveComponent(String className) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        //在此处添加要识别的注解，也是每次扫描的顺序，最好遵循习惯
        addNewAnnotation(MyController.class, aClass);
        addNewAnnotation(MyService.class, aClass);
        addNewAnnotation(MyRepository.class, aClass);
        addNewAnnotation(MyBean.class,aClass);
    }

    private static <T extends Annotation> void addNewAnnotation(Class<T> annotationClass, Class<?> aClass) throws ClassNotFoundException {
        //类上是否存在注解
        if (!AnnotationUtils.isEmpty(aClass.getAnnotation(annotationClass))) {
            Field[] fields = aClass.getDeclaredFields();
            if (fields.length == 0) {
                ListAddUtils.add(componentList, aClass.getName());
            } else {
                if (isEmptyAutowired(fields)) {
                    ListAddUtils.add(componentList, aClass.getName());
                } else {
                    //如果属性上有MyAutoWired继续
                    for (Field field : fields) {
                        if (field.getAnnotation(MyAutowired.class) != null) {
                            String newFieldName = field.getType().getName();
                            //如果是接口，使用实现类注入
                            if (Class.forName(newFieldName).isInterface()) {
                                String nextName = convertInterfaceToImpl(newFieldName);
                                if (!componentList.contains(nextName)) {
                                    resolveComponent(nextName);
                                }
                            } else {
                                //递归调用
                                resolveComponent(newFieldName);
                            }
                        }
                    }
                }
                ListAddUtils.add(componentList, aClass.getName());
            }
        }
        //如果是需要动态的代理注入的接口，加入到实例化的链表中
        else if (aClass.isInterface() && interfaceAndImplMap.get(aClass.getName()).equals("proxy")) {
            ListAddUtils.add(componentList, aClass.getName());
        }
    }

    /**
     * 判断属性是否存在 MyAutowired 注解
     */
    private static boolean isEmptyAutowired(Field[] fields) {
        for (Field field : fields) {
            if (!AnnotationUtils.isEmpty(field.getAnnotation(MyAutowired.class))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 接口类转为实现类,没有实现类的返回‘proxy’
     */
    private static String convertInterfaceToImpl(String newFileName) {
        Set<Map.Entry<String, String>> entries = interfaceAndImplMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (newFileName.equals(entry.getKey()) && !entry.getValue().equals("proxy")) {
                return entry.getValue();
            } else if (newFileName.equals(entry.getKey()) && entry.getValue().equals("proxy")) {
                return entry.getKey();
            }

        }
        return null;
    }

    /**
     * 组装接口和实现类
     */
    private static void makeInterfaceAndImplMap(List<String> classNameList) {
        Class<?> aClass = null;
        //interfaceNameList是所有接口类名的链表
        List<String> interfaceNameList = new ArrayList<>();
        //这个链表保存的是有实现类的接口的链表名，默认没有实现类的接口即为需要动态注的链表
        List<String> interfaceExist = new ArrayList<>();
        //循环类名，将类名注入到链表中
        for (String className : classNameList) {
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            assert aClass != null;
            if (aClass.isInterface()) {
                interfaceNameList.add(aClass.getName());
            }
        }
        for (String className : classNameList) {
            Class<?> bClass = null;
            try {
                bClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            assert bClass != null;
            Class<?>[] interfaces = bClass.getInterfaces();
            if (interfaces != null && interfaces.length != 0) {
                for (String interfaceName : interfaceNameList) {
                    for (Class<?> interfaceClass : interfaces) {
                        //如果既有接口，也有实现类，则组成map
                        if (interfaceName.equals(interfaceClass.getName())) {
                            interfaceAndImplMap.put(interfaceName, className);
                            interfaceExist.add(interfaceName);
                        }
                    }
                }
            }
        }
        //需要动态代理注入的接口，在map中用value = proxy来识别
        interfaceNameList.removeAll(interfaceExist);
        if (interfaceNameList.size() > 0) {
            for (String spareInterfaceName : interfaceNameList) {
                interfaceAndImplMap.put(spareInterfaceName, "proxy");
            }
//            System.out.println("已经存在的" + interfaceNameList);
        }
    }

    /**
     * 返回 类名的集合
     */
    private static List<String> getClassName(String packageName) {
        Enumeration<URL> urls = null;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        String newPackageName = packageName.replace(".", "/");
        try {
            urls = contextClassLoader.getResources(newPackageName);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File packageFile = new File(url.getPath());
                File[] files = packageFile.listFiles();
                if (files == null) {
                    break;
                }
                for (File file : files) {
                    //如果是class,添加到List
                    if (file.getName().endsWith(".class")) {
                        String templeName = (packageName.replace("/", ".") + "." + file.getName());
                        String newTempleName = templeName.substring(0, templeName.lastIndexOf("."));
                        listClassName.add(newTempleName);
                    } else {
                        String nextPackageName = newPackageName + "." + file.getName();
                        getClassName(nextPackageName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listClassName;
    }

    public static void main(String[] args) {
        ScanUtil.getClassName("factory").forEach(System.out::println);
    }
}
