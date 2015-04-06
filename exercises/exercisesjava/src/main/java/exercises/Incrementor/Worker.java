package exercises.Incrementor;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Worker implements Runnable {
    Runnable task = null;
    Incrementor incrementor;

    public Worker(Incrementor incrementor) {
        this.incrementor = incrementor;
    }

    @Override
    public void run() {
        try {
            while (!incrementor.isStopped()) {
                synchronized (this) {
                    if (task != null) {
                        try {
                            task.run();
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                        incrementor.addWorker(this);
                    }
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setTask(Runnable task) {
        this.task = task;
        notify();
    }
}
