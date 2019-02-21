package data_structures;

import java.util.Iterator;

/**
 * Created by zhuran on 2019/2/21 0021
 */
public class MyLinkedList<T> {
    private int size;
    private Node<T> head;
    private Node<T> tail;

    private MyLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private static class Node<T> {
        Node<T> pre;
        Node<T> next;
        T data;

        Node(T data) {
            this.data = data;
        }
    }

    private T removeTail() {
        if (size == 0) {
            return null;
        } else if (size == 1) {
            size--;
            T data = tail.data;
            tail = head = null;
            return data;
        } else {
            size--;
            T data = tail.data;
            tail = tail.pre;
            tail.next = null;
            return data;
        }
    }

    private void addBefore(T o, Node<T> pre) {
        Node<T> node = new Node<>(o);
        Node<T> oldPre = pre.pre;
        oldPre.next = node;
        node.pre = oldPre;
        pre.pre = node;
        node.next = pre;
        size++;
    }

    private void addIndex(T o, int index) {
        Node<T> node = get(index);
        addBefore(o, node);
    }

    private void removeIndex(int index) {
        Node<T> node = get(index);
        node.pre.next = node.next;
        node.next.pre = node.pre;
        size--;
    }

    private void addHead(T o) {
        Node<T> node = new Node<>(o);
        if (size == 0) {
            this.head = node;
            this.tail = node;
            size++;
        } else {
            head.pre = node;
            node.next = head;
            head = node;
            size++;
        }
    }

    private void addTail(T o) {
        Node<T> node = new Node<>(o);
        if (size == 0) {
            this.head = node;
            this.tail = node;
            size++;
        } else {
            node.pre = tail;
            this.tail.next = node;
            tail = node;
            size++;
        }
    }

    public Node<T> get(int index) {
        if (index >= size) {
            return null;
        }
        if (index < (size >> 1)) {
            Node<T> x = head;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<T> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.pre;
            return x;
        }
    }

    public Iterator<T> iterator() {
        return new MyLinkedListIterator();
    }

    private class MyLinkedListIterator implements Iterator<T> {

        private Node<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    public static void main(String[] args) {
        MyLinkedList<Integer> myLinkedList = new MyLinkedList<>();
        myLinkedList.addHead(1);
        myLinkedList.addHead(2);
        Node node = myLinkedList.get(1);
        myLinkedList.addBefore(100, node);
        myLinkedList.addIndex(200, 1);
        myLinkedList.removeIndex(1);
        myLinkedList.addTail(4);
        myLinkedList.addTail(3);
        Iterator iterator = myLinkedList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println("------------------------");
        Node end = reverse(myLinkedList.head);
        while (end!=null){
            System.out.println(end.data);
            end = end.next;
        }
        System.out.println("------------------------");
    }

    public static Node reverse(Node node) {

        Node dummy = new Node(-1);
        dummy.next = node;
        Node pre = dummy.next;
        Node cur = pre.next;
        while (cur != null){
            pre.next = cur.next;
            cur.next = dummy.next;
            dummy.next = cur;
            cur = pre.next;
        }
        return dummy.next;
    }
}
