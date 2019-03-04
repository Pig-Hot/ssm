package design_mode.observer;

import org.omg.CORBA.Object;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class O1 implements Observer {
    @Override
    public void update() {
        System.out.println("update 1");
    }
}
