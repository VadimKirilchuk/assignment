package exercises.IncrementorQueueVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 07.04.2015.
 */
public class Main {
    public static void main(String[] args) {
        Incrementor incrementor = Incrementor.incrementor;
        List<Client> clientList = new ArrayList<>();
        incrementor.init(3);
        for (int i = 0; i < 50; i++) {
            Client client = new Client();
            clientList.add(client);
            Thread thread = new Thread(client);
            thread.start();
        }

        try {

            Thread.currentThread().sleep(50);
            incrementor.shutDown();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
