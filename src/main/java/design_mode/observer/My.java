package design_mode.observer;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class My extends AbstractSubject{

    @Override
    public void operation() {
        System.out.println("update self");
        notifiy();
    }
}
