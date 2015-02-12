package exercises;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Андрей on 09.02.2015.
 */
public class WorkWithClientThread implements  Runnable{
    private Socket socket;

    public WorkWithClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            try {

                InputStream input = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                Scanner scanner = new Scanner(input);
                PrintWriter writer = new PrintWriter(out,true);
                while (scanner.hasNextLine()) {
                    String string = scanner.nextLine();
                    System.out.println("test");
                    writer.println("echo " + string);
                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
