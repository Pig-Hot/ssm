package factory;

import resource.XmlApplicationContext;

import java.util.List;
import java.util.Map;

/**
 * Created by zhuran on 2019/1/16 0016
 * 用于获取容器类的集合
 */
public class BeanDefinition extends XmlApplicationContext {

    /**
     *获取注解容器类集合
     */
    public List<String> getComponentList(String contextConfigLocation){
        List<String> componentList = super.getComponentList(contextConfigLocation);
        return componentList;
    }

    /**
     * 获取XML配置容器类集合
     */
    public Map<String, GenericBeanDefinition> getBeanDefinitionXmlMap(String contextConfigLocation){
        Map<String, GenericBeanDefinition> beanDefinitionXmlMap  = super.getBeanDefinitionMap(contextConfigLocation);
        return beanDefinitionXmlMap;
    }

}
