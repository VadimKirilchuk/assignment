package exercises.IncrementorQueueVersion;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Worker implements Runnable {
    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Task task = Incrementor.incrementor.getTask();
                synchronized (task) {
                    int value = task.getOldValue();
                    value = value + 1;
                    task.setNewValue(value);
                    task.notifyTaskHolder();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }
}
