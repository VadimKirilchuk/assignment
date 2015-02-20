package ru.assignment.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Sender {
    private Socket clientSocket;
    private PrintWriter writer;
    private Scanner scanner;

    public Sender(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        writer = new PrintWriter(clientSocket.getOutputStream());
        scanner=new Scanner(System.in);
    }

    public void startSender() {
        try {
                while (scanner.hasNextLine()) {
                    System.out.println("sender");
                    String message = scanner.nextLine();
                    System.out.println(message);
                    writer.write(message);
                    if (message.equalsIgnoreCase("disconnect")) {
                        break;
                    }
                }

        } finally {
          closeSender();
        }
    }
    public void closeSender(){
        writer.close();
        scanner.close();


    }
}
