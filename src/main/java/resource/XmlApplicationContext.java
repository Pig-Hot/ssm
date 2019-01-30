package resource;

import constants.Constants;
import exception.XmlException;
import factory.ApplicationContext;
import factory.ChildBeanDefinition;
import factory.GenericBeanDefinition;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import rules.IocRules;
import utils.ScanUtil;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhuran on 2019/1/16 0016
 * 封装解析XML方法，模仿IOC注入BeanDefinition
 */
@Slf4j
public class XmlApplicationContext {

    /**
     * 将xml中的bean元素注入到容器中的方法
     */
    protected Map<String, GenericBeanDefinition> getBeanDefinitionMap(String contextConfigLocation) {
        //存储Bean元素信息
        Map<String, GenericBeanDefinition> beanDefinitionXmlMap = new ConcurrentHashMap<>();
        //遍历XML文件获取所有节点
        List<Element> elementsList = getElements(contextConfigLocation);
        //获取Bean节点,注入到beanDefinitionXmlMap中
        for (Element element : elementsList) {
            if (element.getName().equals("bean")) {
                //声明一个bean的map,用来盛放当前bean元素的容器(GenericBeanDefinition)
                GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
                //bean元素的属性容器
                List<ChildBeanDefinition> childBeanDefinitionList = new ArrayList<>();
                String beanId = element.attributeValue(IocRules.BEAN_RULE.getName());
                String beanClass = element.attributeValue(IocRules.BEAN_RULE.getValue());
                //元素是否存在,存在就尝试注入容器
                if (!StringUtils.isEmpty(beanId) && !StringUtils.isEmpty(beanClass)) {
                    genericBeanDefinition.setClassName(beanClass);
                    //获取当前element下所有子元素
                    List<Element> elements = element.elements();
                    if (elements != null) {
                        for (Element childrenElement : elements) {
                            //根据set规则注入容器
                            if (childrenElement.getName().equals(IocRules.SET_INJECT.getType())) {
                                ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition();
                                childBeanDefinition.setChildrenType(IocRules.SET_INJECT.getType());
                                String name = IocRules.SET_INJECT.getName();
                                String value = IocRules.SET_INJECT.getValue();
                                setChildBeanDefinitionByType(childrenElement, childBeanDefinition, name, value, childBeanDefinitionList);
                            } else if (childrenElement.getName().equals(IocRules.CONS_INJECT.getType())) {
                                ChildBeanDefinition childBeanDefinition = new ChildBeanDefinition();
                                childBeanDefinition.setChildrenType(IocRules.CONS_INJECT.getType());
                                String name = IocRules.CONS_INJECT.getName();
                                String value = IocRules.CONS_INJECT.getValue();
                                setChildBeanDefinitionByType(childrenElement, childBeanDefinition, name, value, childBeanDefinitionList);
                            } else {
                                log.info("{}不存在子元素", beanId);
                            }
                            genericBeanDefinition.setChildBeanDefinitionList(childBeanDefinitionList);
                            beanDefinitionXmlMap.put(beanId, genericBeanDefinition);
                        }
                    }
                }
            }
        }
        return beanDefinitionXmlMap;
    }

    private void setChildBeanDefinitionByType(Element childrenElement, ChildBeanDefinition childBeanDefinition, String name, String value, List<ChildBeanDefinition> childBeanDefinitionList) {
        if (childBeanDefinition != null) {
            childBeanDefinition.setAttributeOne(childrenElement.attributeValue(name));
            childBeanDefinition.setAttributeTwo(childrenElement.attributeValue(value));
            childBeanDefinitionList.add(childBeanDefinition);
        } else {
            try {
                throw new XmlException("未按照格式配置xml文件或者暂不支持改配置属性");
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Element> getElements(String contextConfigLocation) {
        //创建saxReader对象
        SAXReader saxReader = new SAXReader();
        //通过read方法读取一个文件 转换成Document对象
        Document document = null;
        //解析文件的路径
        String pathName = Constants.PATH + contextConfigLocation;
        try {
            document = saxReader.read(pathName);
        } catch (DocumentException e) {
            log.error("文件没有被找到,{}", e);
        }
        //获取Root节点
        Element element = document.getRootElement();
        //获取所有字节点
        List<Element> elementsList = element.elements();
        return elementsList;
    }

    /**
     * 根据XML,获得注解扫描的bean容器
     */
    public List<String> getComponentList(String contextConfigLocation) {
        List<String> componentList = new ArrayList<>();
        List<Element> elementList = getElements(contextConfigLocation);
        for (Element element : elementList) {
            if (element.getName().equals(IocRules.SNAN_RULE.getType())) {
                String packageName = element.attributeValue(IocRules.SNAN_RULE.getName());
                //根据packageName获取注解注入的beanName
                componentList.addAll(resolveComponentList(packageName));
            }
        }
        return componentList;
    }

    /**
     * 根据要扫描的包名，返回有注解扫描的类
     */
    public List<String> resolveComponentList(String packageName) {

        if (StringUtils.isEmpty(packageName)) {
            try {
                throw new XmlException("请正确设置" + IocRules.SNAN_RULE.getType() + "的属性");
            } catch (XmlException e) {
                e.printStackTrace();
            }
        }
        List<String> componentList = new ArrayList<>();
        //通过扫描工具类扫描所有有注解的类
        List<String> componentListAfter = ScanUtil.getComponentList(packageName);
        componentList.addAll(componentListAfter);
        return componentList;
    }
}
