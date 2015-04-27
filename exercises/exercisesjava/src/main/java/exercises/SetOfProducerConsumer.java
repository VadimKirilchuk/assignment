package exercises;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Андрей on 24.04.2015.
 */
public class SetOfProducerConsumer {
    private static int[] array;
    private static Semaphore semaphoreEmpty;
    private static Semaphore semaphoreFull;
    private static List<Thread> threadList = new LinkedList<>();

    public static void main(String[] args) {
        array = new int[10];
        semaphoreEmpty = new Semaphore(array.length);
        semaphoreFull = new Semaphore(0);

        for (int i = 0; i < 15; i++) {
            Consumer consumer = new Consumer();
            Thread thread = new Thread(consumer);
            threadList.add(thread);
        }
        for (int i = 0; i < 15; i++) {
            Producer producer = new Producer();
            Thread thread = new Thread(producer);
            threadList.add(thread);
        }

        for(Thread thread:threadList){
            thread.start();
        }
        try {
            Thread.sleep(100);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        for(Thread thread:threadList){
            thread.interrupt();
        }


    }

    private static class Producer implements Runnable {
        static int index = 0;

        @Override
        public void run() {
            int rand = (int) (Math.random() * 10);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    semaphoreEmpty.acquire();
                    synchronized (semaphoreEmpty) {
                        System.out.println("producer"+ Thread.currentThread());
                        array[index] = rand;
                        index = (index + 1) % array.length;
                    }
                    semaphoreFull.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class Consumer implements Runnable {
        static int index = 0;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    semaphoreFull.acquire();
                    synchronized (semaphoreFull) {
                        System.out.println(array[index]);
                        index = (index + 1) % array.length;
                    }
                    semaphoreEmpty.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
