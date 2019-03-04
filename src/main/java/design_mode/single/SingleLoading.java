package design_mode.single;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public class SingleLoading {
    private static SingleLoading singleLoading;
    private static final Object lock = new Object();

    private SingleLoading() {

    }

    public static SingleLoading getInstace() {
        if (singleLoading == null) {
            synchronized (lock) {
                if (singleLoading == null) {
                    return new SingleLoading();
                }
            }
        }
        return singleLoading;
    }
}
