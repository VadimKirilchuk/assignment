package group.idru.assignment;

import java.util.Stack;

public class TwoStackQueue {
    public static void main(String[] args) {

        Queue<String> queueInstance = new Queue<>(3);
        queueInstance.push("1");

        queueInstance.push("2");
        queueInstance.push("3");
        queueInstance.pop();
        queueInstance.push("5");
        System.out.println(queueInstance.pop());

    }


}

class Queue<E> {
    private Stack<E> stackQueue1 = new Stack<>();
    private Stack<E> stackQueue2 = new Stack<>();
    private int capacity;

    public Queue(int capacity) {

        this.capacity = capacity;
    }

    public String toString() {

        return "stackQueue2 " + stackQueue2.toString() + "stackQueue1 " + stackQueue1.toString();

    }

    public void push(E num) {

        if (stackQueue1.size() + stackQueue2.size() == capacity)
            throw new IllegalStateException("Stack is full");

        stackQueue1.push(num);


    }


    public E pop() {
        if (stackQueue1.isEmpty() && stackQueue2.isEmpty()) {
            throw new IllegalStateException("Stack is full");

        }
        if (stackQueue2.isEmpty()) {

            while (!stackQueue1.isEmpty()) {

                stackQueue2.push(stackQueue1.pop());
            }

            return stackQueue2.pop();
        } else {
            return stackQueue2.pop();
        }
    }


}


