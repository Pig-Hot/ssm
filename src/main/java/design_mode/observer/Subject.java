package design_mode.observer;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public interface Subject {
    void add(Observer observer);
    void del(Observer observer);
    void notifiy();
    void operation();
}
