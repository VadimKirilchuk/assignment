package ru.assignment.net;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Sender {
    private final Socket clientSocket;
    private final OutputStreamWriter writer;
    private final Scanner scanner;
    private volatile boolean  isOpen = false;
    private final DisconnectReceivedListener listener;
    private Thread currentThread;

    public Sender(Socket clientSocket, DisconnectReceivedListener listener) throws IOException {
        ChatClient.clientLogger.debug("Configuration sender constructor ");
        this.listener=listener;
        this.clientSocket = clientSocket;
        writer = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_16);
        scanner = new Scanner(System.in);
        currentThread=Thread.currentThread();
    }
    private void sendMessageToServer() throws IOException {
        if (scanner.hasNextLine()) {
            String message = scanner.nextLine() + "\n";
            ChatClient.clientLogger.debug("Sender send message to server, message: {}",message);
            writer.write(message);
            writer.flush();
            if (message.equalsIgnoreCase("close" + "\n")) {
                isOpen = false;
            }
        } else {
            isOpen = false;
        }
    }
    /**
     * first we check the information from console, if we haven't data,
     * we will sleep for 10 mil. and check data available one more time
     * After getting data to our input stream we should read string and send  string to output stream.
     * So we can terminate  program execution  in two cases:
     * 1. when sender was manually closed , here we get exception on (System.in.available(),
     *  break the loop and close();
     * 2. when receiver ask to terminate sender via disconnect(), in this case we can get 2 situation
     *  a)we sleep, so we get InterruptedException, break the loop and safely close sender
     *  b)we make some translation job inside sendMessageToServer(), so we will  send data to space, check that
     *  isOpen = true and safely shut down the execution
     *  After all closing manipulations we should go back and check that receiver and sender were closed.
     */
    public void connect() {

        isOpen = true;
        ChatClient.clientLogger.debug("Sender waiting message from user, sender isOpen- {}",isOpen);
        while (isOpen) {
            try {
                if (System.in.available() == 0) {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException a) {
                        ChatClient.clientLogger.debug("Interrupt sender at sleep operation");
                        break;
                    }
                } else {
                    sendMessageToServer();
                }
            } catch (IOException e) {
                ChatClient.clientLogger.debug("Sender exception -{}",e);
                isOpen = false;
            }
        }
        close();
        listener.disconnectReceived();
        /*
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
                    System.out.println("exception");
                    e.printStackTrace();
                    break;
                }

                if (message.equalsIgnoreCase("close" + "\n")) {
                    System.out.println("exit sender close");
                    break;
                }
            }
        } finally {
            if (this.isOpen()) {
                close();
            }
        }
*/
    }



   public boolean isOpen() {
      return isOpen;
    }

    public void disconnect() {
        ChatClient.clientLogger.debug("Sender disconnect");
        currentThread.interrupt();
        isOpen = false;
    }

    private void close() {
        ChatClient.clientLogger.debug("Close sender");
        scanner.close();
        try {
            writer.close();
        } catch (IOException e) {
            ChatClient.clientLogger.debug("Sender's writer close exception - {}",e);
        }
    }
}
