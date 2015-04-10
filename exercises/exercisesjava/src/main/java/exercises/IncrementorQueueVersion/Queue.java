package exercises.IncrementorQueueVersion;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Queue {
    private LinkedList<Task> taskList;
    private volatile boolean stop = false;

    public Queue() {
        taskList = new LinkedList<>();
    }

    public synchronized Task getTask() throws InterruptedException {
        while (taskList.isEmpty()) {
            wait();
        }
        return taskList.removeFirst();
    }

    public synchronized void addTask(Task task) {
        if (!stop) {
            taskList.add(task);
            notify();
        } else {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void cleanQueue() {
        stop = true;
        Iterator<Task> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            synchronized (task) {
                task.notifyTaskHolder();
                task.interrupt();
            }
            iterator.remove();
        }
    }
}
