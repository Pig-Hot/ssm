package design_mode.observer;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class Test {
    public static void main(String[] args) {
        Subject sub = new My();
        sub.add(new O1());
        sub.add(new O2());
        sub.notifiy();
    }
}
