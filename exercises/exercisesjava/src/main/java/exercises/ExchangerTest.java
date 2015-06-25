package exercises;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Андрей on 25.04.2015.
 */
public class ExchangerTest {
    private static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(new Receiver()).start();
        new Thread(new Sender()).start();
        ;
    }

    private static class Sender implements Runnable {
        @Override
        public void run() {
            try {


                String string = exchanger.exchange("sender",0, TimeUnit.MICROSECONDS);
                System.out.println(string);
            } catch (InterruptedException e) {
                System.out.println("sender interrupt");
            }catch (TimeoutException e){
                System.out.println("sender timeoutexception");
            }
        }
    }

    private static class Receiver implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                String string = exchanger.exchange("receiver");
                System.out.println(string);
            } catch (InterruptedException e) {
                System.out.println("receiver interrupt");
            }
        }
    }
}
