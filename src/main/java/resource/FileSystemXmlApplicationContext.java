package resource;

import exception.XmlException;
import factory.GenericBeanDefinition;

import java.util.Map;

/**
 * Created by zhuran on 2019/1/16 0016
 */
public class FileSystemXmlApplicationContext extends XmlApplicationContext {
    public Map<String, GenericBeanDefinition> getGenericBeanDefinition(String contextConfigLocation){

        Map<String, GenericBeanDefinition>  genericBeanDefinition  = super.getBeanDefinitionMap(contextConfigLocation);

        return genericBeanDefinition;
    }
}
