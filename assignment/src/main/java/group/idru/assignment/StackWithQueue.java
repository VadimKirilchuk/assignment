package group.idru.assignment;

import java.util.LinkedList;

public class StackWithQueue {
    public static void main(String[] args) {

        MainStack<String> stackInstance = new MainStack<>(3);

        stackInstance.push("1");
        stackInstance.push("2");
        stackInstance.push("3");
        System.out.println(stackInstance);
        stackInstance.pop();
        stackInstance.pop();
        stackInstance.pop();
        stackInstance.pop();

        System.out.println(stackInstance);

    }


}


class MainStack<E> {
    private int capacity;

    private LinkedList<E> queueFirst = new LinkedList<>();
    private LinkedList<E> queueSecond = new LinkedList<>();


    public MainStack(int capacity) {
        this.capacity = capacity;


    }

    public String toString() {

        return queueFirst.toString();
    }

    public void push(E elem) {

        if (queueFirst.size() == capacity) {
            throw new IllegalStateException("Stack is full");
        }
        queueFirst.add(elem);

    }

    public E pop() {

        if (queueFirst.isEmpty()) {
            throw new IllegalStateException("Stack is full");

        }
        while (queueFirst.size() != 1) {
            queueSecond.add(queueFirst.remove());
        }

        E obj = queueFirst.remove();

        while (!queueSecond.isEmpty()) {
            queueFirst.add(queueSecond.remove());
        }

        return obj;


    }

    public boolean isEmpty() {

        return queueFirst.isEmpty();
    }

}


