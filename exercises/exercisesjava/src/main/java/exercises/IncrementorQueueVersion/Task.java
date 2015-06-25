package exercises.IncrementorQueueVersion;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Task {
    private int oldValue;
    private int newValue;
    private volatile boolean ready = false;
    private volatile boolean isInterrupred = false;

    public Task(int value) {
        oldValue = value;
        newValue = value;
    }

    public int getOldValue() {
        return oldValue;
    }

    public synchronized int getNewValue() {
        try {
            while (!ready) {
                wait();
            }
            if (isInterrupred) {
                throw new InterruptedException();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return newValue;
    }

    public  void setNewValue(int value) {
        newValue = value;
    }

    public void notifyTaskHolder() {
        ready = true;
        notify();
    }

    public void interrupt() {
        isInterrupred = false;
    }
}
