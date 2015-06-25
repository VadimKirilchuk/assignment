package exercises.Incrementor;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Client implements Runnable {
    Incrementor incrementor;

    public Client(Incrementor incrementor) {
        this.incrementor = incrementor;
    }

    @Override
    public void run() {
     Random random =new Random();
        Task task = new Task(random.nextInt(51));
        incrementor.addTask(task);
        System.out.println("old " + task.getOldValue() + " new " + task.getNewValue() );
    }
}
