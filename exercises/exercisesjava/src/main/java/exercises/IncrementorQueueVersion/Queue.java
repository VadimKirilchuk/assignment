package exercises.IncrementorQueueVersion;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Queue {
    private LinkedList<Task> taskList;

    public Queue() {
        taskList = new LinkedList<>();
    }

    public Task getTask() throws InterruptedException {

        while (taskList.isEmpty()) {
            wait();
        }

        return taskList.removeFirst();
    }

    public void addTask(Task task) {
        taskList.add(task);
        notify();
    }


    public void cleanQueue() {

        Iterator<Task> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            synchronized (task) {
                task.setFlag();
                task.notify();
            }
            iterator.remove();
        }
    }
}
