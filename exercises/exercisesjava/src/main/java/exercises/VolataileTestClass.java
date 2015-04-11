package exercises;

/**
 * Created by Андрей on 10.04.2015.
 */
public class VolataileTestClass {

    public static void main(String [] args){
        TargetClass object=new TargetClass();
        Thread thread=new Thread(object);
        thread.start();
        try{
Thread.sleep(100);
            object.cancel();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }


    private static class TargetClass implements Runnable{
        private volatile boolean cancel=false;

        @Override
        public void run(){
            int i=0;
            while (!cancel){
                System.out.println(i++);
            }
        }

        public void cancel(){
            cancel=true;

        }
    }
}


