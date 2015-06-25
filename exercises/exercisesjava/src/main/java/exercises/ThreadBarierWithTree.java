package exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 14.04.2015.
 */
public class ThreadBarierWithTree {
    static List<WorkerThread> threadObjectsList = new ArrayList<>();
    static int[] arrayForCheck = new int[5];
    static int[] condition = new int[5];

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            WorkerThread thread = new WorkerThread(i);
            threadObjectsList.add(thread);

        }


        for (WorkerThread thread :threadObjectsList ) {

            thread.start();
        }

    }

    public static class WorkerThread extends Thread {
        public final int index;

        public WorkerThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread());

            if ((index + 1) > (threadObjectsList.size() / 2)) {
               // System.out.println(index+1);
                synchronized (this) {
                    arrayForCheck[index] = 1;
                    notify();
                    while (condition[index] != 1) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    condition[index] = 0;
                }
               System.out.println("frrrreeee list " + index);
            } else {

                if (index == 0) {
                    if (threadObjectsList.size() != 1) {
                       // System.out.println(index+1);
                        int indexForSecondChild;
                        int indexForFirtsChild;
                        indexForFirtsChild = ((index + 1) * 2) - 1;
                       // System.out.println("root "+(index+1)+" child  rod "+(indexForFirtsChild+1));
                        WorkerThread secondObjectForSynchronization = null;
                        WorkerThread objectForSynchronization = null;

                        objectForSynchronization = threadObjectsList.get(indexForFirtsChild);
                        synchronized (objectForSynchronization) {
                            while (arrayForCheck[indexForFirtsChild] != 1) {
                                try {
                                    objectForSynchronization.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                         //   System.out.println("root "+(index+1)+" child  rod "+(indexForFirtsChild+1)+"after wait");
                            arrayForCheck[indexForFirtsChild] = 0;
                        }

                        if ((indexForSecondChild = ((index + 1) * 2))
                                < threadObjectsList.size()) {
                          //  System.out.println("root "+(index+1)+" child  rod "+(indexForSecondChild+1));
                            secondObjectForSynchronization = threadObjectsList.get(indexForSecondChild);
                            synchronized (secondObjectForSynchronization) {
                                while (arrayForCheck[indexForSecondChild] != 1) {
                                    try {
                                        secondObjectForSynchronization.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                arrayForCheck[indexForSecondChild] = 0;
                            }
                        }
                        synchronized (objectForSynchronization) {
                            condition[indexForFirtsChild] = 1;
                            objectForSynchronization.notify();
                            if (secondObjectForSynchronization != null) {
                                synchronized (secondObjectForSynchronization) {
                                    condition[indexForSecondChild] = 1;
                                    secondObjectForSynchronization.notify();
                                }
                            }
                        }
                    }
                    System.out.println("frrrreeee root" + index);
                } else
                //index>0 but less then n/2
                {

                    int indexForSecondChild;
                    int indexForFirtsChild;
                    indexForFirtsChild = ((index + 1) * 2) - 1;
                    System.out.println("rod "+(index+1)+" child first "+ (indexForFirtsChild+1));
                    WorkerThread secondObjectForSynchronization = null;
                    WorkerThread objectForSynchronization = null;

                    objectForSynchronization = threadObjectsList.get(indexForFirtsChild);
                    synchronized (objectForSynchronization) {
                        while (arrayForCheck[indexForFirtsChild] != 1) {
                            try {
                                objectForSynchronization.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                      //  System.out.println("rod "+(index+1)+" child first connect ");
                        arrayForCheck[indexForFirtsChild] = 0;
                    }

                    if ((indexForSecondChild = ((index + 1) * 2))
                            < threadObjectsList.size()) {
                       // System.out.println("rod "+(index+1)+" child second "+ (indexForSecondChild+1));
                        secondObjectForSynchronization = threadObjectsList.get(indexForSecondChild);
                        synchronized (secondObjectForSynchronization) {
                            while (arrayForCheck[indexForSecondChild] != 1) {
                                try {
                                    secondObjectForSynchronization.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        //    System.out.println("rod "+(index+1)+" child second connect ");
                            arrayForCheck[indexForSecondChild] = 0;
                        }
                    }

                    synchronized (this) {
                        arrayForCheck[index] = 1;
                        notify();
                        while (condition[index] != 1) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                          //  System.out.println("rod "+(index+1)+" rod condition ok ");
                        }
                        condition[index] = 0;
                        synchronized (objectForSynchronization) {
                            condition[indexForFirtsChild] = 1;
                            objectForSynchronization.notify();
                            if (secondObjectForSynchronization != null) {
                                synchronized (secondObjectForSynchronization) {
                                    condition[indexForSecondChild] = 1;
                                    secondObjectForSynchronization.notify();
                                }
                            }
                        }
                    }
                    System.out.println("frrrreeee rod " + index);
                }
            }
        }
    }
}

