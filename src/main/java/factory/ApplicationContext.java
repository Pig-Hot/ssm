package factory;

import lombok.extern.slf4j.Slf4j;
import resource.FileSystemXmlApplicationContext;
import rules.IocRules;
import utils.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by zhuran on 2019/1/16 0016
 * 上下文类
 */
@Slf4j
public class ApplicationContext extends FileSystemXmlApplicationContext implements BeanFactory {

    private Map<String, GenericBeanDefinition> subMap = null;

    public ApplicationContext(String contextConfigLocation) {
        this.subMap = super.getGenericBeanDefinition(contextConfigLocation);
    }

    /**
     * 获取Bean方法
     */
    public Object getBean(String beanId) {
        Object object = null;
        Class<?> aClass = null;
        Set<Map.Entry<String, GenericBeanDefinition>> entries = subMap.entrySet();
        // 判断容器中是否存在beanId
        if (subMap.containsKey(beanId)) {
            // 如果存在。开始遍历每一个bean
            for (Map.Entry<String, GenericBeanDefinition> entrie : entries) {
                // 如果beanId在容器中找到了
                if (beanId.equals(entrie.getKey())) {
                    // 声明一个容器中的子对象，用来保存子元素
                    GenericBeanDefinition genericBeanDefinition = entrie.getValue();
                    String beanName = genericBeanDefinition.getClassName();
                    // 此对象的意思是对象的属性集合
                    List<ChildBeanDefinition> childBeanDefinitionList = genericBeanDefinition.getChildBeanDefinitionList();
                    // 实例化对象
                    try {
                        aClass = Class.forName(beanName);
                    } catch (ClassNotFoundException e) {
                        log.error("{}没有找到", beanName);
                        e.printStackTrace();
                    }

                    try {
                        object = aClass.newInstance();
                    } catch (InstantiationException e) {
                        log.info("无参实例化对象异常");
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    // 遍历属性集合
                    for (ChildBeanDefinition childBeanDefinition : childBeanDefinitionList) {
                        // 如果xml中的属性和IocRules中定义的setRule属性一致，则使用set注入
                        if (IocRules.SET_INJECT.getType().equals(childBeanDefinition.getChildrenType())) {
                            setValue(aClass, childBeanDefinition, object);
                        }
                        // 如果符合构造器注入的规则，则使用构造器注入
                        else if (IocRules.CONS_INJECT.getType().equals(childBeanDefinition.getChildrenType())) {
                            // 构造器注入需要同时注入所有属性
                            List<ChildBeanDefinition> constructorChildBeanDefinition = new ArrayList<>();
                            for (ChildBeanDefinition conChildBeanDefinition : childBeanDefinitionList) {
                                if (IocRules.CONS_INJECT.getType().equals(conChildBeanDefinition.getChildrenType())) {
                                    constructorChildBeanDefinition.add(conChildBeanDefinition);
                                }
                            }
                            object = consValue(aClass, constructorChildBeanDefinition, object);
                        }
                    }
                }
            }
        }
        return object;
    }

    private Object consValue(Class<?> aClass, List<ChildBeanDefinition> constructorChildBeanDefinition, Object object) {
        //定义Constructor
        Constructor constructor = null;
        //获取所有属性
        Field[] fields = aClass.getDeclaredFields();
        //定义一个Class[]用于存放实例化所需参数类型
        Class<?>[] classArray = new Class[fields.length];
        //定义一个Object[]用于存放实例化所需参数
        Object[] objects = new Object[constructorChildBeanDefinition.size()];
        //根据AttributeTwo进行排序
        Collections.sort(constructorChildBeanDefinition);
        for (int i = 0; i < objects.length; i++) {
            objects[i] = constructorChildBeanDefinition.get(i).getAttributeOne();
        }
        for (int i = 0; i < fields.length; i++) {
            switch (fields[i].getType().getSimpleName()) {
                case "String":
                    classArray[i] = String.class;
                    break;
                case "Integer":
                    classArray[i] = Integer.class;
                    break;
                case "int":
                    classArray[i] = Integer.class;
                    break;
            }
        }
        try {
            //获取对应Constructor
            constructor = aClass.getConstructor(classArray);
            //实例化对象
            object = constructor.newInstance(objects);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void setValue(Class aClass, ChildBeanDefinition childBeanDefinition, Object object) {
        Field field = null;
        Field[] fields = aClass.getDeclaredFields();
        Method[] methods = null;
        String type = null;
        String propertyName = childBeanDefinition.getAttributeOne();
        String propertyValue = childBeanDefinition.getAttributeTwo();
        String methodName = "set" + StringUtils.fristCharToLowerCase(propertyName);
        try {
            field = aClass.getDeclaredField(propertyName);
            type = field.getType().getSimpleName();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        methods = aClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                try {
                    assert type != null;
                    switch (type) {
                        case "String":
                            method.invoke(object, propertyValue);
                            break;
                        case "Integer":
                            method.invoke(object, Integer.valueOf(propertyName));
                            break;
                        case "int":
                            method.invoke(object, Integer.valueOf(propertyName));
                            break;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
