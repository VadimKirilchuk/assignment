package exercises;

import java.util.concurrent.Semaphore;

/**
 * Created by Андрей on 17.04.2015.
 */
public class ProducerConsumer {

    public static  void main(String [] args){
        Buffer buffer=new Buffer();
        Producer producer=new Producer(buffer);
        Consumer consumer=new Consumer(buffer);
        producer.start();
        consumer.start();


    }




    private static class Buffer {
        int array[] = new int[5];
        int i = 0;
        Semaphore semaphoreProduse = new Semaphore(1);
        Semaphore semaphoreConsume = new Semaphore(0);

        public void get() throws InterruptedException {
            semaphoreConsume.acquire();



                System.out.println("get " + array[i - 1]);


            semaphoreProduse.release();
        }

        public void set() throws InterruptedException {
            semaphoreProduse.acquire();

            array[i] = i++;


                System.out.println("set " + array[i - 1]);

            semaphoreConsume.release();
        }
    }

    private static class Producer extends Thread {
        Buffer buffer;

        Producer(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            int i = 5;
            while (i > 0) {
                try {
                    buffer.set();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
        }
    }

    private static class Consumer extends Thread {
        Buffer buffer;

        Consumer(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            int i = 5;
            while (i > 0) {
                try {
                    buffer.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
        }
    }
}
