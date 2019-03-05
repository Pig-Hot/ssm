package design_mode.proxy.cglib;

import design_mode.proxy.IUser;
import design_mode.proxy.UserServImpl;

/**
 * Created by zhuran on 2019/3/5 0005
 */
public class Test {
    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        IUser iUser = (IUser) cglibProxy.getInstance(new UserServImpl());
        iUser.findAllUser();
    }
}
