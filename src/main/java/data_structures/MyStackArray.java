package data_structures;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * Created by zhuran on 2019/2/21 0021
 */
public class MyStackArray {
    private Object[] elementData;
    private int top;
    private int size;
    public MyStackArray(){
        this.size = 10;
        this.top = -1;
        elementData = new Object[size];
    }
    public MyStackArray(int size){
        if(size>0) {
            this.size = size;
            elementData = new Object[size];
            this.top = -1;
        }else {
            throw new IllegalArgumentException("栈初始容量不能小于0:" + size);
        }
    }
    public void push(Object o){
        if(top+1>=size){
            size = size * 2;
            if(size<Integer.MAX_VALUE) {
                elementData = Arrays.copyOf(elementData, size);
            }else {
                elementData = Arrays.copyOf(elementData, Integer.MAX_VALUE);
            }
            push(o);
        }else {
            elementData[++top] = o;
        }
    }
    public Object pop(){
        Object o = peek();
        remove(top);
        return o;
    }
    public Object peek(){
        if(top == -1){
            throw new EmptyStackException();
        }
        return elementData[top];
    }
    public boolean isEmpty(){
        return (top == -1);
    }

    public void remove(int top) {
        elementData[top] = null;
        this.top--;
    }

    public void display(){
        for (Object o:elementData){
            System.out.println(o);
        }
    }
    public static void main(String[] args) throws Exception{
        MyStackArray stackArray = new MyStackArray();
        stackArray.push(1);
        stackArray.push(2);
        System.out.println(stackArray.pop());
        stackArray.push(3);
        System.out.println(stackArray.pop());
        stackArray.display();
    }
}
