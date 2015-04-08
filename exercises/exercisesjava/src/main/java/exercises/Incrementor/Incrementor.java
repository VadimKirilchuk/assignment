package exercises.Incrementor;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Incrementor implements Runnable {
    private LinkedList<Task> taskList;
    private LinkedList<Worker> workerList;
    private volatile boolean stopped = false;
    private Thread currentThread;

    public Incrementor() {
        taskList = new LinkedList<>();
        workerList = new LinkedList<>();
    }

    public void init(int workerCount) {
        for (int i = 0; i < workerCount; i++) {
            Worker worker = new Worker(this);
            workerList.add(worker);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }

    @Override
    public void run() {
        currentThread = Thread.currentThread();

        while (!stopped && !currentThread.isInterrupted()) {
            Task task = null;
            try {

                synchronized (taskList) {
                    while (taskList.isEmpty()) {
                        taskList.wait();
                    }
                    task = getTask();
                }

                synchronized (workerList) {
                    while (workerList.isEmpty()) {

                        workerList.wait();
                    }
                    getWorker().setTask(task);
                }
            } catch (InterruptedException e) {
                currentThread.interrupt();
                if (task != null) {
                    task.interrupt();
                }
            }
        }

;
    }

    public void removeTasks() {
        int i = 0;

        Iterator<Task> iterator = taskList.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            task.interrupt();
            iterator.remove();
        }


    }

    public void addWorker(Worker worker) {

        synchronized (workerList) {
            if (!stopped) {
                workerList.addLast(worker);
                workerList.notify();
            }
        }
    }

    public Worker getWorker() {

        return workerList.removeLast();
    }

    public void addTask(Task task) {

        synchronized (taskList) {
            if (!stopped) {
                taskList.add(task);
                taskList.notify();
            } else {
                task.interrupt();
            }
        }
    }

    public Task getTask() {

        return taskList.removeFirst();
    }

    public boolean isStopped() {
        return stopped;
    }

    public void terminate() {
        synchronized (taskList) {

            synchronized (workerList) {

                removeTasks();
                removeWorkers();
                currentThread.interrupt();
                stopped = true;
            }
        }
    }

    public void removeWorkers() {

        Iterator<Worker> iterator = workerList.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            synchronized (worker) {
                worker.getThread().interrupt();
            }
            iterator.remove();
        }
    }
}

