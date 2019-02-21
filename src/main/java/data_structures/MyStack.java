package data_structures;

import java.util.LinkedList;

/**
 * Created by zhuran on 2019/2/21 0021
 */
public class MyStack<T> {
    private LinkedList<T> linkedList;

    public MyStack() {
        this.linkedList = new LinkedList<>();
    }

    public void push(T data) {
        linkedList.add(data);
    }

    public T pop() {
        T data = linkedList.getLast();
        linkedList.removeLast();
        return data;
    }

    public void display() {
        for (Object aLinkedList : linkedList) {
            System.out.println(aLinkedList);
        }
    }

    public static void main(String[] args) {
        MyStack<Integer> stack = new MyStack<>();
        stack.push(1);
        stack.push(2);
        System.out.println(stack.pop());
        stack.push(3);
        stack.display();
    }
}
