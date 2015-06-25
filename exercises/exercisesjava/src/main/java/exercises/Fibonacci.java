package exercises;

/**
 * Created by Андрей on 26.03.2015.
 */
public class Fibonacci {
    public static void main (String[] args)throws InterruptedException{
        for(int i=1;i<=5;i++) {
            Object obj;
            Thread thread=new Thread(new Fibo(12));
            thread.start();


        }
    }

}

class Fibo implements Runnable{
    int count;
    Fibo(int count )throws InterruptedException{
        this.count=count;
    }
    public void run(){

        int first=1;
        int second=1;
        for(int i=2;i<count;i++){
            int variable=second;
            second=second+first;
            first=variable;
        }
        System.out.println(second+" thread "+Thread.currentThread().getName());

    }
}
