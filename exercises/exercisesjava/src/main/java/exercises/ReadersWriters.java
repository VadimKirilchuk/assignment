package exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Андрей on 18.04.2015.
 */
public class ReadersWriters {
    private static String string = "hello";
    private static List<Thread> threadList = new ArrayList<>();

    public static void main(String[] args) {

        int [][] f=new int[3][5];
        System.out.println(f.length);
        Semaphore sem = new Semaphore(-1);

        try {
            sem.acquire();
            sem.release();
            sem.release();
            sem.release();
            //sem.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(sem.availablePermits() + "   hhhhhhhh");

        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < 5; i++) {
            Writers writer = new Writers(semaphore);
            threadList.add(writer);
            Readers reader = new Readers(semaphore);
            threadList.add(reader);
        }
        for (int i = 0; i < 5; i++) {
            // Readers  reader=new Readers(semaphore);
            // threadList.add(reader);
        }
        for (Thread thread : threadList) {
            thread.start();
        }
    }

    private static class Readers extends Thread {
        private Semaphore semaphore;
        private static int count = 0;

        private Readers(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            synchronized (ReadersWriters.class) {
                count++;
                if (count == 1) {
                    try {
                        System.out.println("before acquire " + Thread.currentThread()+count);
                        semaphore.acquire();
                        System.out.println("after acquire " + Thread.currentThread()+ count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread());
            }

            System.out.println("reader " + string + " count "+count);
            synchronized (ReadersWriters.class) {
                System.out.println(Thread.currentThread()+" sinchro- count "+count);
                count--;
                if (count == 0) {
                    semaphore.release();
                    System.out.println("release reader"+ Thread.currentThread());
                }
            }
        }
    }

    private static class Writers extends Thread {
        Semaphore semaphore;

        private Writers(Semaphore semaphore) {

            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                string = "test";
                System.out.println("writer " + string);
                Thread.sleep(300);
                semaphore.release();
                System.out.println("release writer");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
