package factory;

import java.util.Comparator;

/**
 * Created by zhuran on 2019/1/16 0016
 * Bean的具体属性
 */
public class ChildBeanDefinition implements Comparable<ChildBeanDefinition> {
    private String childrenType;
    private String attributeOne;
    private String attributeTwo;

    public String getChildrenType() {
        return childrenType;
    }

    public void setChildrenType(String childrenType) {
        this.childrenType = childrenType;
    }

    public String getAttributeOne() {
        return attributeOne;
    }

    public void setAttributeOne(String attributeOne) {
        this.attributeOne = attributeOne;
    }

    public String getAttributeTwo() {
        return attributeTwo;
    }

    public void setAttributeTwo(String attributeTwo) {
        this.attributeTwo = attributeTwo;
    }


    @Override
    public int compareTo(ChildBeanDefinition o) {
        return Integer.valueOf(this.getAttributeTwo()) - Integer.valueOf(o.getAttributeTwo());
    }

}
