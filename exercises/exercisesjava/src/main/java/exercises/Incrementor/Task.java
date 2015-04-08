package exercises.Incrementor;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Task implements Runnable {
    private int oldValue;
    private int newValue;
    private boolean flag = false;

    public Task(int value) {
        oldValue = value;
        newValue=value;
    }

    @Override
    public synchronized void run() {
        newValue = newValue + 1;
        flag = true;
        notify();
    }

    public synchronized int getNewValue() {
        while (!flag) {
            try {
                wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        return newValue;
    }

    public int getOldValue(){
        return oldValue;
    }
    public synchronized void interrupt(){
        flag=true;
        notify();
    }
}
