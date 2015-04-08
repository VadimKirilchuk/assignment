package exercises.IncrementorQueueVersion;

import java.util.Random;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Client implements Runnable {
    private volatile boolean  stop=false;
    @Override
    public void run() {
        while (!stop) {
            Random random = new Random();
            Task task = new Task(random.nextInt(51));
            Incrementor.incrementor.addTask(task);
            System.out.println("old " + task.getOldValue() + " new " + task.getNewValue());
        }
    }

    public void stop(){
        stop=true;
    }
}
