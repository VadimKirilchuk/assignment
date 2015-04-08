package exercises.IncrementorQueueVersion;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Task {
    private int oldValue;
    private int newValue;
    private volatile boolean flag = false;

    public Task(int value) {
        oldValue = value;
        newValue=value;
    }

    public int getOldValue() {
        return oldValue;
    }

    public synchronized int getNewValue() {
        try {
            while (!flag) {
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return newValue;
    }

    public synchronized void setNewValue() {
        newValue = newValue+1;
        flag=true;
        notify();
    }

    public void setFlag(){
        flag=true;
    }
}
