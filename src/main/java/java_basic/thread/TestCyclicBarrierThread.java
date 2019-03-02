package java_basic.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrierThread {
    public static void main(String[] args) {
        int num = 5;
        CyclicBarrier barrier = new CyclicBarrier(num, new Leader());
        for (int i = 0; i < num; i++) {
            new Thread(new Employee(barrier)).start();
        }
    }
}

class Leader implements Runnable {
    @Override
    public void run() {
        System.out.println("吃饭前我先说几句");
        try {
            Thread.sleep(5000);//说了半个小时  比如吧
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Employee implements Runnable {
    private CyclicBarrier barrier;

    public Employee(final CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void run() {
        try {
            System.out.println("都在等领导说完话准备吃");
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("说了30分钟准备用筷子开始吃了");
    }
}