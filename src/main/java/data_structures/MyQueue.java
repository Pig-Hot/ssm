package data_structures;

import java.util.LinkedList;

/**
 * Created by zhuran on 2019/2/21 0021
 */
public class MyQueue {
    private LinkedList linkedList;
    public MyQueue(){
        linkedList = new LinkedList();
    }
    public void put(Object o){
        linkedList.add(o);
    }
    public Object poll(){
        Object o = linkedList.getFirst();
        linkedList.removeFirst();
        return o;
    }
    public void display(){
        linkedList.forEach(System.out::println);
    }

    public static void main(String[] args) {
        MyQueue queue = new MyQueue();
        queue.put(1);
        queue.put(2);
        queue.put(3);
        queue.put(4);
        queue.poll();
        queue.poll();
        queue.display();
    }
}
