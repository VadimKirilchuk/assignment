/*
 * Two queue stack implementation.
 * Version 1.0
 */

package ru.assignment.collections;
import java.util.LinkedList;

public class StackWithQueueAlternativ {
    public static void main(String[] args) {
        MainStack<String> stackInstance = new MainStack<>(3);
        stackInstance.push("1");
        stackInstance.push("2");
        stackInstance.push("3");
        System.out.println(stackInstance);
        System.out.println(stackInstance.pop());
    }
}

/**
 * Stack implementation based on two queues.
 * This class provides methods for working with stack.
 * This implementation is alternative version of StackWithQueue,
 * it makes some copy job while pushing, to take an object without
 * any problem.
 * @param <E>
 */
class StackClass<E> {
    private int capacity;
    private LinkedList<E> queueFirst = new LinkedList<>();
    private LinkedList<E> queueSecond = new LinkedList<>();

    public StackClass(int capacity) {
        this.capacity = capacity;
    }

    public String toString() {
        return queueFirst.toString();
    }

    /* Method for putting object to queue */
    public void push(E object) {
        if (queueFirst.size() == capacity) {
            throw new IllegalStateException("Stack is full");
        }
        if (queueFirst.isEmpty()) {
            queueFirst.add(object);
        } else {
            while (!queueFirst.isEmpty()) {
                queueSecond.add(queueFirst.remove());
            }
            queueFirst.add(object);
            while (!queueSecond.isEmpty()) {
                queueFirst.add(queueSecond.remove());
            }
        }
    }

    /* Method for getting object to queue */
    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is Empty");
        }
        return queueFirst.remove();
    }

    public boolean isEmpty() {
        return queueFirst.isEmpty();
    }
}


