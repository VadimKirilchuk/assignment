package exercises;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Андрей on 16.04.2015.
 */
public class DiningPhilosophers {
    private static Philosopher[] arrayOfPhilosophers = new Philosopher[3];
    private static Fork[] arrayOfForks = new Fork[3];

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            arrayOfPhilosophers[i] = new Philosopher(i);
            arrayOfForks[i] = new Fork();
        }
        for (Philosopher philosopher : arrayOfPhilosophers) {
            executor.submit(philosopher);
        }
        executor.shutdown();
    }

    private static class Fork {
        public boolean inWork = false;

        public boolean get() {
            if (!inWork) {
                inWork = true;
                return inWork;
            }
            return !inWork;
        }

        public void put() {
            inWork = false;
        }
    }

    private static class Philosopher implements Runnable {
        private int index;
        private int rightPhilosopher;
        private int leftPhilosopher;

        Philosopher(int index) {
            this.index = index;
            rightPhilosopher = (index + 1) % arrayOfPhilosophers.length;
            leftPhilosopher = (index + (arrayOfPhilosophers.length - 1)) % arrayOfPhilosophers.length;
        }

        @Override
        public void run() {
            int i = 2;
            while (i > 0) {
                if (checkForks()) {
                    System.out.println(" " + index);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (arrayOfForks[index]) {
                        synchronized (arrayOfForks[leftPhilosopher]) {
                            arrayOfForks[index].put();

                            arrayOfForks[leftPhilosopher].put();
                            arrayOfForks[index].notify();
                            arrayOfForks[leftPhilosopher].notify();
                        }
                    }
                }
                i--;
            }
        }

        private boolean checkForks() {
            if (index % 2 == 0) {
                synchronized (arrayOfForks[index]) {
                    if (!arrayOfForks[index].get()) {
                        try {

                            arrayOfForks[index].wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
                synchronized (arrayOfForks[leftPhilosopher]) {
                    if (arrayOfForks[leftPhilosopher].get()) {

                        return true;
                    } else {
                        try {
                            synchronized (arrayOfForks[index]) {
                                arrayOfForks[index].put();
                                arrayOfForks[index].notify();
                            }
                            arrayOfForks[leftPhilosopher].wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            } else {
                synchronized (arrayOfForks[leftPhilosopher]) {
                    if (!arrayOfForks[leftPhilosopher].get()) {
                        try {

                            arrayOfForks[leftPhilosopher].wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
                synchronized (arrayOfForks[index]) {
                    if (arrayOfForks[index].get()) {

                        return true;
                    } else {
                        try {
                            synchronized (arrayOfForks[leftPhilosopher]) {
                                arrayOfForks[leftPhilosopher].put();
                                arrayOfForks[leftPhilosopher].notify();
                            }

                            arrayOfForks[index].wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            }
        }
    }
}

