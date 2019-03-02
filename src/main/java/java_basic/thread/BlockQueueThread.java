package java_basic.thread;

import java.util.concurrent.*;

/**
 * Created by zhuran on 2019/3/1 0001
 */
public class BlockQueueThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue<Integer>(6);
        Cunsumer cunsumer = new Cunsumer(arrayBlockingQueue);
        Producer producer = new Producer(arrayBlockingQueue);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future future = executorService.submit(cunsumer);
        executorService.submit(producer);
        System.out.println("总共消费 " + future.get());

    }
}

class Cunsumer implements Callable {
    private ArrayBlockingQueue arrayBlockingQueue;
    Cunsumer(ArrayBlockingQueue arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }
    @Override
    public Object call() throws Exception {
        int result = 0;
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            int j = (int) arrayBlockingQueue.take();
            System.out.println("消费 " + j);
            result += j;
        }
        return result;
    }
}

class Producer implements Runnable {
    private ArrayBlockingQueue arrayBlockingQueue;
    Producer(ArrayBlockingQueue arrayBlockingQueue) {
        this.arrayBlockingQueue = arrayBlockingQueue;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                arrayBlockingQueue.put(i);
                System.out.println("提供 " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}