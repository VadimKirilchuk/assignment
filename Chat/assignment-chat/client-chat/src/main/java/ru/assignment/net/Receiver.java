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
        ChatClient.clientLogger.debug("Configuration receiver constructor");
        this.listener = listener;
        this.clientSocket = clientSocket;
        streamReader = new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_16);
        reader = new BufferedReader(streamReader);
    }

    public void connectAsync() {
        ChatClient.clientLogger.debug("Start new receiver thread");
        Thread receiverThread = new Thread(this);
        receiverThread.start();
    }

    public void run() {
        ChatClient.clientLogger.debug("Receiver run");
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
        ChatClient.clientLogger.debug("Start waiting messages from server, receiver isOpen -{}",isOpen);
        while (isOpen) {

            try {
                if (!reader.ready()) {

                    try {

                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        ChatClient.clientLogger.debug("Interrupt receiver at sleep operation");
                        break;
                    }
                } else {
                    ChatClient.clientLogger.debug("Receiver is ready to get data");
                    String message = reader.readLine();
                    System.out.println(message);
                    ChatClient.clientLogger.debug("Receiver get data from server: {}",message);
                    if (message.equals("disconnect")) {
                        isOpen = false;
                    }
                }
            } catch (IOException e) {
                ChatClient.clientLogger.debug("Receiver exception- {}",e);
                isOpen = false;
            }
        }
        close();
        listener.disconnectReceived();
    }
public boolean isOpen(){
    return isOpen;
}
    public void disconnect() {
        ChatClient.clientLogger.debug("Disconnect receiver");
        currentThread.interrupt();
        isOpen = false;
    }

    public void close() {
        ChatClient.clientLogger.debug("Close receiver");
        try {

            reader.close();
        } catch (IOException e) {
            ChatClient.clientLogger.debug("Receiver reader close exception- {}",e);
        }
    }
}
