/*
 * Two queue stack implementation.
 * Version 1.0
 */

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

/**
 * Stack implementation based on two queues.
 * This class provides methods for working with stack.
 * This implementation is alternative version of StackWithQueueAlternativ,
 * it makes some copy job while pop is necessary, but pushing is simple.
 *
 * @param <E>
 */
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

    public void push(E element) {
        if (queueFirst.size() == capacity) {
            throw new IllegalStateException("Stack is full");
        }
        queueFirst.add(element);
    }

    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is Empty");
        }
        while (queueFirst.size() != 1) {
            queueSecond.add(queueFirst.remove());
        }
        E object = queueFirst.remove();
        while (!queueSecond.isEmpty()) {
            queueFirst.add(queueSecond.remove());
        }
        return object;
    }

    public boolean isEmpty() {
        return queueFirst.isEmpty();
    }
}


