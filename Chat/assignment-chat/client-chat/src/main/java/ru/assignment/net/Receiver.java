package ru.assignment.net;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Receiver implements Runnable {
    private Socket clientSocket;
    private Sender sender;
    private Scanner reader;

    public Receiver(Socket clientSocket, Sender sender) throws IOException {
        this.clientSocket = clientSocket;
        reader = new Scanner(clientSocket.getInputStream());
        this.sender = sender;
    }

    public void run() {
        System.out.println("run receiver");
        listen();
    }

    public void listen() {
        try {
            System.out.println("receiver wait message");
            while (reader.hasNextLine()) {

                String message = reader.nextLine();
                System.out.println("get message to receiver "+message);
                System.out.println(message);
            }
        } finally {
            closeReceiver();
        }
    }

    public void closeReceiver() {
        System.out.println("close receiver");
        reader.close();
        if(sender.isOpen()){
        sender.closeSender();
        }
    }
}
