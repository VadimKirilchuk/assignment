package exercises;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Андрей on 01.04.2015.
 */
public class ReadWriteLock {
    public void main(String[] args) {

    }

    private final Map<Thread, Integer> readersMap = new HashMap<>();
    private Thread currentWriterThread = null;
    private int writerRequestCounter = 0;
    private int writerAccessCounter = 0;

    public synchronized void lockRead() throws InterruptedException {

        while (checkPermissionForReader(Thread.currentThread())) {
            wait();
        }
        addReader(Thread.currentThread());
    }

    private boolean checkPermissionForReader(Thread currentThread) {
        if (Thread.currentThread() == currentWriterThread) {
            return false;
        }
        if (currentWriterThread != null) {
            return true;
        }
        if (readersMap.get(currentThread) != null) {
            return false;
        }
        if (writerRequestCounter > 0) {
            return true;
        }
        return false;
    }

    private void addReader(Thread thread) {
        Integer value = readersMap.get(thread);
        if (value != null) {
            readersMap.put(thread, value.intValue() + 1);
        } else {
            readersMap.put(thread, 1);
        }
    }

    public synchronized void unlockRead() {
        Integer threadCounter = readersMap.get(Thread.currentThread());
        if (threadCounter == null)
            throw new IllegalMonitorStateException();
        if (threadCounter == 1) {
            readersMap.remove(Thread.currentThread());
        } else {
            readersMap.put(Thread.currentThread(), threadCounter.intValue() - 1);
        }
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        writerRequestCounter++;
        while (checkPermissionForWriter()) {
            wait();
        }
        writerRequestCounter--;
        currentWriterThread = Thread.currentThread();
        writerAccessCounter++;
    }

    private boolean checkPermissionForWriter() {
        if (Thread.currentThread() == currentWriterThread || (readersMap.get(Thread.currentThread()) != null)) {
            return false;
        }
        if (currentWriterThread != null || readersMap.size() > 0) {
            return true;
        }
        return false;
    }

    public synchronized void unlockWrite() {
        if (currentWriterThread == null) {
            throw new IllegalMonitorStateException();
        }
        if (writerAccessCounter == 1) {
            currentWriterThread = null;
        }
        writerAccessCounter--;
        notifyAll();
    }
}
