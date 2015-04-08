package exercises.Incrementor;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Worker implements Runnable {
    Task task = null;
    Thread currentTgread;
    Incrementor incrementor;

    public Worker(Incrementor incrementor) {
        this.incrementor = incrementor;
    }

    @Override
    public void run() {
        currentTgread = Thread.currentThread();
        try {
            synchronized (this) {
                while (!incrementor.isStopped()) {
                    if (task == null) {
                        wait();
                    } else {
                        try {
                            task.run();
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                        incrementor.addWorker(this);
                        task = null;
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (task != null) {
                task.interrupt();
            }
        }

    }

    public synchronized void setTask(Task task) {
        if (!incrementor.isStopped()) {
            this.task = task;
        } else {
            task.interrupt();
        }
        notify();
    }

    public synchronized Thread getThread() {
        return currentTgread;
    }
}
