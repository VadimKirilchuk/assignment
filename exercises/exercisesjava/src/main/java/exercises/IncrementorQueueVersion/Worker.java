package exercises.IncrementorQueueVersion;

import java.io.IOException;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Worker implements Runnable {




    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Task task = Incrementor.incrementor.getTask();
                task.setNewValue();
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

    }
}
