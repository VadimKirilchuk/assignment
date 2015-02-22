package ru.assignment.net;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Sender {
    private Socket clientSocket;
    private OutputStreamWriter writer;
    private Scanner scanner;
    private boolean isOpen = false;

    public Sender(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        writer = new OutputStreamWriter(clientSocket.getOutputStream());
        scanner = new Scanner(System.in);
    }

    public void startSender() {
        isOpen = true;
        try {
            System.out.println("StartSender wait message from console");
            while (scanner.hasNextLine()) {
                System.out.println("sender next line ");
                String message = scanner.nextLine() + "\n";
                System.out.println("sender get message- " + message);
                try {
                    writer.write(message);
                    writer.flush();
                    System.out.println("Sender write message");
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                if (message.equalsIgnoreCase("disconnect" + "\n")) {
                    System.out.println("exit sender disconnect");
                    break;
                }
            }
        } finally {
            if (this.isOpen()) {
                closeSender();
            }
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void closeSender() {
        System.out.println("closeSender");
        isOpen = false;
        scanner.close();
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
