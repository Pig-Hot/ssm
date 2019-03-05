package design_mode.proxy.jdk;

import design_mode.proxy.IUser;
import design_mode.proxy.UserServImpl;

/**
 * Created by zhuran on 2019/3/5 0005
 */
public class Test {
    public static void main(String[] args) {
        JDKProxy jdkProxy = new JDKProxy();
        IUser iUser = (IUser) jdkProxy.createProxyInstance(new UserServImpl());
        iUser.findAllUser();
    }
}
