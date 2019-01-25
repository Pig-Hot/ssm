package test.boot;

import factory.InitBean;
import test.controller.TestController;

/**
 * Created by zhuran on 2019/1/24 0024
 */
public class TestBootStrap {
    public static void main(String[] args) {
        InitBean initBean = new InitBean();
        initBean.initBeans();
        TestController testController = (TestController) initBean.beanContainerMap.get("test.controller.TestController");
        testController.test();
    }
}
