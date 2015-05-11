package exercises;

import java.util.ArrayList;

/**
 * Created by Андрей on 25.04.2015.
 */
public class Gener {
    public static void main(String[] args) {
    }

    public <S> S sh(S f) {
        S d = (S) new Object();
        return d;
    }
}

class JJ extends Gener {
    public static void main(String[] args) {
    }

    public <S> S sh(S f) {
        S d = (S) new Object();
        return d;
    }
}

class RQ100_02 {
    public static void main(String[] args) {
        String[] lst = {"Java", "only", "promotes", "fun"};
        System.out.println(lst.equals(sh(lst)));
    }

    public static String[][] sh(String[]... d) {
        return d;
    }
}

class App {
    static volatile int data = 0;
    static volatile boolean run = true;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {

                }
                data = 1;
                run = false;
            }
        }).start();
        System.out.println("ddd" + run);
        while (run) {

        /*NOP*/
        }
        ArrayList<Long> g=new ArrayList<>();
        g.add(1l);
        System.out.println(data);
    }
}
