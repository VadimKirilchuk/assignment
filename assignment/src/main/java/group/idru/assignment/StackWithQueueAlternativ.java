package group.idru.assignment;

import java.util.LinkedList;

public class StackWithQueueAlternativ {
    public static void main(String[] args) {

        StackClass<String> stackInstance = new StackClass<>(3);

        stackInstance.push("1");
        stackInstance.push("2");
        stackInstance.push("3");
        System.out.println(stackInstance);
        System.out.println(stackInstance.pop()
        );

    }


}


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

    public E pop() {

        if (queueFirst.isEmpty()) {
            throw new IllegalStateException("Stack is Empty");
        }
        return queueFirst.remove();
    }

    public boolean isEmpty() {

        return queueFirst.isEmpty();
    }

}


