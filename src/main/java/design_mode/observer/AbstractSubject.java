package design_mode.observer;

import java.util.ArrayList;

/**
 * Created by zhuran on 2019/3/4 0004
 */
public abstract class AbstractSubject implements Subject {
    private ArrayList<Observer> observers = new ArrayList<>();

    @Override
    public void add(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void del(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifiy() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
