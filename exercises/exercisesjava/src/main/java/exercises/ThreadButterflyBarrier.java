package exercises;

/**
 * Created by Андрей on 15.04.2015.
 */
public class ThreadButterflyBarrier {
    private static WorkerThread[] threadArray = new WorkerThread[11];
    private static int[] threadConditionArray = new int[11];

    public static void main(String[] args) {

        for (int i = 0; i < 11; i++) {

            threadArray[i] = new WorkerThread(i);
        }

        for (WorkerThread thread : threadArray) {

            thread.start();
        }
    }

    private static class WorkerThread extends Thread {
        private int index;

        WorkerThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread());
            breakBarrier();
        }

        public void breakBarrier() {
            int levelCount = getIterationCount();
            for (int i = 1; i <= levelCount; i++) {
                int indexOfTargetThread = getTargetThreadIndex(i);
                synchronized (this) {
                    threadConditionArray[index] = threadConditionArray[index] + 1;
                    notifyAll();
                }
                synchronized (threadArray[indexOfTargetThread]) {
                    while (threadConditionArray[indexOfTargetThread] < threadConditionArray[index]) {
                        try {
                            threadArray[indexOfTargetThread].wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            System.out.println(" leave barrier " + Thread.currentThread());
        }

        private int getTargetThreadIndex(int loopLevel) {
            int threadIndex = index + pow(loopLevel - 1);
            if (threadIndex >= threadArray.length) {
                threadIndex = (threadIndex + 1) % threadArray.length;
                threadIndex--;
            }
            //   System.out.println("index "+index+" target index "+threadIndex +" looplevel "+ loopLevel);
            return threadIndex;
        }

        private int pow(int number) {
            return 1 << number;
        }

        private int getIterationCount() {
            int count = threadArray.length;
            count = count - 1;
            count |= (count >> 1);
            count |= count >> 2;
            count |= count >> 4;
            count |= count >> 8;
            count |= count >> 16;
            count = (count + 1);
            return 31 - Integer.numberOfLeadingZeros(count);
        }
    }
}
