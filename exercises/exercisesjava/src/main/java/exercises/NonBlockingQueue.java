package exercises;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Андрей on 16.05.2015.
 */
public class NonBlockingQueue {
    private class Queue<E> {
        AtomicReference<Node<E>> head = new AtomicReference<>(new Node<E>(null, null));
        AtomicReference<Node<E>> tail = head;

        private class Node<E> {
            E element;
            AtomicReference<Node<E>> next;

                       Node(E element, Node<E> next) {
                this.element = element;
                this.next = new AtomicReference<>(next);
            }
        }

        public boolean push(E element) {
            Node<E> newElement = new Node<>(element, null);

            while (true) {
                Node<E> queueTail = tail.get();
                Node<E> lastReference = queueTail.next.get();
                if (queueTail == tail.get()) {
                    if (lastReference == null) {
                        if (queueTail.next.compareAndSet(null, newElement)) {
                            tail.compareAndSet(queueTail, newElement);
                            return true;
                        }
                    } else {
                        tail.compareAndSet(queueTail, lastReference);
                    }
                }
            }
        }

        public E poll() {
            while (true) {
                Node<E> queueHead = head.get();
                Node<E> queueTail = tail.get();
                Node<E> queueHeadNext = queueHead.next.get();

                if (queueHead == head.get()) {
                    if (queueHead == queueTail) {
                        if (queueHeadNext == null) {
                            return null;
                        } else {
                            tail.compareAndSet(queueTail, queueHeadNext);
                        }
                    } else {
                        head.compareAndSet(queueHead, queueHeadNext);
                        E element = queueHeadNext.element;
                        return element;
                    }
                }
            }
        }
    }
}


