package exercises;

/**
 * Created by Андрей on 14.04.2015.
 */
public class ThreadBarier {
    static int[] arrayFirst = new int[5];
    static int[] condition = new int[5];
    public static void main(String [] args ){
        ThreadCoordinator coordinator=new ThreadCoordinator();
        coordinator.start();
        for(int i=0;i<5;i++){
            WorkerThread thread=new WorkerThread(i);
            thread.start();
        }
    }




    private static  class WorkerThread extends Thread{
        private int index;

        WorkerThread(int index){
            this.index=index;
        }

        @Override
        public void run(){

            System.out.println(Thread.currentThread());
            synchronized (arrayFirst){
                arrayFirst[index]=1;
                arrayFirst.notify();
            }
            synchronized (condition){
                while (condition[index]!=1){
                    try {
                        condition.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread()+"condition");
                    condition[index]=0;
                }
            }


        }
    }


    private static class ThreadCoordinator extends Thread {
        @Override
        public void run() {
            synchronized (arrayFirst) {
                for (int i = 0; i < arrayFirst.length; i++) {
                    checkThread(i);
                }
            }
            synchronized (condition){
                for (int i=0;i<condition.length;i++){
                    condition[i]=1;
                }
                condition.notifyAll();
                System.out.println("freeeeeeeeeeedom");
            }
        }

        private void checkThread(int i) {
            try {
                while (arrayFirst[i] != 1) {
                    arrayFirst.wait();
                }
                arrayFirst[i] = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
