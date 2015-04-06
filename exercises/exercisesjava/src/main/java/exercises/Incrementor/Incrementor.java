package exercises.Incrementor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Incrementor implements Runnable {
    private LinkedList<Runnable> taskList;
    private LinkedList<Worker> workerList;
    private volatile boolean stopped = false;

    public Incrementor(int workerCount) {
        taskList = new LinkedList<>();
        workerList = new LinkedList<>();
        for (int i = 0; i < workerCount; i++) {
            Worker worker = new Worker(this);
            workerList.add(worker);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            while (!stopped) {
                if (taskList.isEmpty()) {
                    synchronized (taskList) {
                        taskList.wait();
                    }
                } else {
                    if (workerList.isEmpty()) {
                        synchronized (workerList) {
                            workerList.wait();
                        }
                    }
                }
                Runnable task = getTask();
                getWorker().setTask(task);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addWorker(Worker worker) {
        synchronized (workerList) {
            workerList.addFirst(worker);
            workerList.notify();
        }
    }

    public Worker getWorker() {
        return workerList.removeLast();
    }

    public void addTask(Task task) {
        synchronized (taskList) {
            taskList.add(task);
            taskList.notify();
        }
    }

    public Runnable getTask() {
        return taskList.remove(0);
    }

    public boolean isStopped() {
        return stopped;
    }

    public void terminate() {
        stopped = true;
        for(Worker worker:workerList){
            synchronized (worker){
                worker.notify();
            }
        }
    }
}

