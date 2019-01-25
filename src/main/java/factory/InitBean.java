package factory;

import annotation.MyAutowired;
import constants.Constants;
import lombok.extern.slf4j.Slf4j;
import mybatis.MyMybatisProxy;
import test.dao.TestDao;
import utils.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhuran on 2019/1/16 0016
 * 初始化
 */
@Slf4j
public class InitBean extends BeanDefinition {

    //初始化后的bean容器 key为class名，value为实例化对象
    public Map<String, Object> beanContainerMap = new ConcurrentHashMap<>();

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
        System.out.println("注解实例化顺序： " + componentList);
        for (String className : componentList) {
            //将每一个类初始化
            try {
                initClass(className);
            } catch (ClassNotFoundException e) {
                log.error("{}没有找到", className);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
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
        System.out.println("实例化的名字" + aClass.getName());
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

    public static void main(String[] args) {
        InitBean initBean = new InitBean();
        initBean.initBeans();
        for (String entry : initBean.beanContainerMap.keySet()) {
            System.out.println(entry);
        }
    }
}
