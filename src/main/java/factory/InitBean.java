package factory;

import annotation.MyAutowired;
import annotation.MyRequest;
import constants.Constants;
import http.model.HttpControllerModel;
import lombok.extern.slf4j.Slf4j;
import mybatis.MyMybatisProxy;
import test.dao.TestDao;
import utils.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhuran on 2019/1/16 0016
 * 初始化
 */
@Slf4j
public class InitBean extends BeanDefinition {

    //初始化后的bean容器 key为class名，value为实例化对象
    public Map<String, Object> beanContainerMap = new ConcurrentHashMap<>();
    //初始化后的Controller容器 key为path,value为方法名和参数的Map
    private List<HttpControllerModel> models = new ArrayList<>();

    public List<HttpControllerModel> getModels() {
        return models;
    }

    public Map<String, Object> getBeanContainerMap() {
        return beanContainerMap;
    }

    /**
     * 初始化bean容器方法
     * 注意，扫描的bean会覆盖xml中配置的bean，spring也是这样，扫描的注入和装配都是在xml之后
     * MyAutowired暂时是根据名称装配和扫描
     */

    public void initBeans() {
        //初始化xml配置
        initXmlBeans();
        //初始化注解配置
        initAutowiredBeans();
    }

    private void initAutowiredBeans() {
        List<String> componentList = super.getComponentList(Constants.contextConfigLocation);
        for (String className : componentList) {
            //将每一个类初始化
            try {
                initClass(className);
                routeSet(className);
            } catch (ClassNotFoundException e) {
                log.error("{}没有找到", className);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置路由,为了方便简单,只允许无参和单个参数
     */
    private void routeSet(String className) throws Exception {
        Class<?> aClass = Class.forName(className);
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(MyRequest.class) != null) {
                HttpControllerModel model = new HttpControllerModel();
                String path = method.getAnnotation(MyRequest.class).path();
                String requestType = method.getAnnotation(MyRequest.class).type();
                if (method.getParameterTypes().length == 0) {
                    model.setClassName(aClass.getName());
                    model.setMethodName(method.getName());
                    model.setParamType(null);
                    model.setPath(path);
                    model.setRequestType(requestType);
                    models.add(model);
                } else {
                    Type type = method.getParameterTypes()[0];
                    model.setClassName(aClass.getName());
                    model.setMethodName(method.getName());
                    model.setParamType(type.getTypeName());
                    model.setPath(path);
                    model.setRequestType(requestType);
                    models.add(model);
                }
            }
        }
    }

    private void initClass(String className) throws Exception {
        Class<?> aClass = Class.forName(className);
        //先判断这个类有没有接口，如果有接口，将接口装配
        Class<?>[] interfaces = aClass.getInterfaces();
        if (aClass.isInterface()) {
            //如果是接口,注入的对象是动态代理的对象
            Object myMybatisProxy = MyMybatisProxy.getObjectProxy(aClass);
            beanContainerMap.put(aClass.getName(), myMybatisProxy);
        } else if (interfaces == null || interfaces.length == 0) {
            //如果不是接口的实现类，也就是controller层
            noInterfaceInit(className, className);
        } else {
            for (Class<?> interfaceClass : interfaces) {
                boolean flag = isExistInContainer(className);
                //容器中如果有，则直接使用这个对象进行装配
                if (flag) {
                    beanContainerMap.put(interfaceClass.getName(), aClass.newInstance());
                } else {
                    //如果容器中没有，则先实例化实现类，然后再装配到容器中
                    noInterfaceInit(className, interfaceClass.getName());
                }
            }
        }
    }

    public void noInterfaceInit(String className, String interfaceName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> aClass = Class.forName(className);
        //bean实例化
        Object object = aClass.newInstance();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            //如果属性上有MyAutowired注解，则先将属性注入进去
            if (!AnnotationUtils.isEmpty(field.getAnnotation(MyAutowired.class))) {
                //System.out.println("发现注解");
                //设置私有属性可见
                field.setAccessible(true);
                //如果有注解，在实例化链表里面搜寻类名
                Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String type = field.getType().getName();
                    if (entry.getKey().equals(type)) {
                        field.set(object, entry.getValue());
                    }
                }
            }
        }
        beanContainerMap.put(interfaceName, object);
    }

    private boolean isExistInContainer(String className) {
        Set<Map.Entry<String, Object>> entries = beanContainerMap.entrySet();
        if (entries != null) {
            for (Map.Entry<String, Object> map :
                    entries) {
                if (map.getKey().equals(className)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void initXmlBeans() {
        ApplicationContext applicationContext = new ApplicationContext(Constants.contextConfigLocation);
        Class<?> aClass = null;
        //从容器中取出bean，用application的getbean方法依次加载bean
        Map<String, GenericBeanDefinition> beanDefinitionMap = super.getBeanDefinitionXmlMap(Constants.contextConfigLocation);
        Set<Map.Entry<String, GenericBeanDefinition>> entries = beanDefinitionMap.entrySet();
        for (Map.Entry<String, GenericBeanDefinition> entry : entries) {
            String beanId = entry.getKey();
            String className = entry.getValue().getClassName();
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                log.error("xml中{}无法实例化", className);
                e.printStackTrace();
            }
            beanContainerMap.put(className, aClass.cast(applicationContext.getBean(beanId)));
        }
    }
}
