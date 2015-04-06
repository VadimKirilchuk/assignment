package exercises.Incrementor;

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
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Task task = new Task(random.nextInt(1,51));
        incrementor.addTask(task);
        System.out.println("old " + task.getOldValue() + " new " + task.getNewValue() );
    }
}
