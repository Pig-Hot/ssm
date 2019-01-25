package factory;

import java.util.List;

/**
 * Created by zhuran on 2019/1/16 0016
 */
public class GenericBeanDefinition {
    /**
     * className和Xml中的class对应
     */
    private String className;
    /**
     * Bean的具体属性
     */
    private List<ChildBeanDefinition> childBeanDefinitionList;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ChildBeanDefinition> getChildBeanDefinitionList() {
        return childBeanDefinitionList;
    }

    public void setChildBeanDefinitionList(List<ChildBeanDefinition> childBeanDefinitionList) {
        this.childBeanDefinitionList = childBeanDefinitionList;
    }
}
