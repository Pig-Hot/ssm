package java_basic.thread;

/**
 * Created by zhuran on 2019/3/1 0001
 */
public class WaitAndNotifiy {
    private int i = 0;
    private static final Object o = new Object();

    class A extends Thread {
        A(String name) {
            this.setName(name);
        }

        @Override
        public void run() {
            for (; i < 100; ) {
                synchronized (o) {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    i++;
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class B extends Thread {
        B(String name) {
            this.setName(name);
        }

        @Override
        public void run() {
            for (; i < 100; ) {
                synchronized (o) {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    i++;
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class C extends Thread {
        C(String name) {
            this.setName(name);
        }

        @Override
        public void run() {
            for (; i < 100; ) {
                synchronized (o) {
                    System.out.println(i + " " + Thread.currentThread().getName());
                    i++;
                    o.notify();
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void start() {
        A a = new A("A");
        B b = new B("B");
        C c = new C("C");
        a.start();
        b.start();
        c.start();
    }

    public static void main(String[] args) {
        WaitAndNotifiy start = new WaitAndNotifiy();
        start.start();
    }
}
