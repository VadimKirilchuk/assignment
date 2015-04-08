package exercises.IncrementorQueueVersion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Incrementor {
    public static Incrementor incrementor = new Incrementor();
    Queue taskQueue;
    List<Thread> threadList;
    public static volatile boolean stop = false;

    private Incrementor() {
        taskQueue = new Queue();
        threadList = new ArrayList<>();
    }

    public void init(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            Worker worker = new Worker();
            Thread thread = new Thread(worker);
            threadList.add(thread);
            thread.start();
        }
    }

    public void addTask(Task task) {
        synchronized (taskQueue) {
            if (!stop) {
                taskQueue.addTask(task);
            } else {
                task.setFlag();
            }
        }
    }

    public Task getTask() throws InterruptedException {
        synchronized (taskQueue) {
            return taskQueue.getTask();
        }
    }

    public void shutDown() {
        synchronized (taskQueue) {
            stop = true;

            taskQueue.cleanQueue();
            cleanThreads();
        }
    }

    public void cleanThreads() {
        Iterator<Thread> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            Thread thread = iterator.next();
            thread.interrupt();
            iterator.remove();
        }
    }
}