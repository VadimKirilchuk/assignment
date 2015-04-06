package exercises.Incrementor;

/**
 * Created by Андрей on 06.04.2015.
 */
public class Main {

    public static void main(String [] args) {
        Incrementor incrementor=new Incrementor(5);
        new Thread(incrementor).start();
        for (int i = 0; i < 21; i++) {
         Client client=   new Client(incrementor);
            new Thread(client).start();


        }
    }
}
