package exercises;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

/**
 * Created by Андрей on 25.04.2015.
 */
public class PhaserAnalize {
    private static Phaser phaser = new Phaser(2);
    private static CountDownLatch countLatch = new CountDownLatch(2);
    private static CyclicBarrier barrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        Thread thread1 = new Thread(new First());
        Thread thread2 = new Thread(new Second());

        thread1.start();
        thread2.start();
    }

    private static class First implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                Thread.sleep(10000);
                    barrier.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }catch (BrokenBarrierException e){
                    e.printStackTrace();
                }

                System.out.println(" first");
       
            }
        }
    }

    private static class Second implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println(" second before ");
                    barrier.await();
                    System.out.println(" second after ");
                } catch (InterruptedException e) {

                }catch (BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
