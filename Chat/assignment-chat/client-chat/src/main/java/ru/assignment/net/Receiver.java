package ru.assignment.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Receiver implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final InputStreamReader streamReader;
    private final DisconnectReceivedListener listener;
    private volatile boolean isOpen = false;
    private Thread currentThread;


    public Receiver(Socket clientSocket, DisconnectReceivedListener listener) throws IOException {
        this.listener = listener;
        this.clientSocket = clientSocket;
        streamReader = new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_16);
        reader = new BufferedReader(streamReader);
    }

    public void connectAsync() {
        Thread receiverThread = new Thread(this);
        receiverThread.start();
    }

    public void run() {
        System.out.println("run receiver");
        currentThread = Thread.currentThread();

        connect();
    }

    /**
     * first we check the information at  our input stream, if we haven't- we should check the EOS,
     * in this case we break the loop and make some job for safely receiver closing
     * if we have another case, without EOS , we will sleep for 10 mil. and check data available one more time
     * After we get data to our input stream we should read string and send  string to console.
     */
    public void connect() {
        /*
        try {
            System.out.println("receiver wait message");
            while (reader.hasNextLine()) {

                String message = reader.nextLine();
                System.out.println("get message to receiver " + message);
                System.out.println(message);
            }
        } finally {
            System.out.println("finally connect");
            disconnect();
        }
*/
        isOpen = true;
        while (isOpen) {

            try {
                if (!reader.ready()) {
                    try {

                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        break;
                    }
                } else {
                    System.out.println("ReadLine");
                    String message = reader.readLine();
                    System.out.println(message);
                    if (message.equals("disconnect")) {
                        isOpen = false;
                    }
                }
            } catch (IOException e) {
                System.out.println("exception");
                e.printStackTrace();
                isOpen = false;
            }
        }
        close();
        listener.disconnectReceived();
    }

    public void disconnect() {
        currentThread.interrupt();
        System.out.println("disconnect receiver");
        isOpen = false;
    }

    public void close() {
        System.out.println("close receiver");
        try {

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
