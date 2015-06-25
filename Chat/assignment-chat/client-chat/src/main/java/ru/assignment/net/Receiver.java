package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Receiver implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final InputStreamReader streamReader;
    private final DisconnectDataListener listener;
    private volatile boolean isOpen = false;
    private Thread currentThread;

    public Receiver(Socket clientSocket, DisconnectDataListener listener) throws IOException {
        LOG.trace("Configuration receiver constructor");
        this.listener = listener;
        this.clientSocket = clientSocket;
        streamReader = new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_16);
        reader = new BufferedReader(streamReader);
    }

    public void connectAsync() {
        LOG.trace("Start new receiver thread");
        Thread receiverThread = new Thread(this);
        receiverThread.start();
    }

    public void run() {
        LOG.debug("Receiver run");
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

        isOpen = true;
        LOG.trace("Start waiting messages from server, receiver isOpen= {}", isOpen);
        while (isOpen) {
            try {
                if (!reader.ready()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        LOG.trace("Interrupt receiver at sleep operation");
                        break;
                    }
                } else {
                    LOG.trace("Receiver is ready to get data");
                    String message = reader.readLine();
                    System.out.println(message);
                    LOG.info("Receiver get data from server: {}", message);
                    if (message.equals("server disconnect")) {
                        isOpen = false;
                    }
                }
            } catch (IOException e) {
                LOG.error("Receiver exception ", e);
                isOpen = false;
            }
        }
        close();
        listener.finishDataOperation();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void disconnect() {
        LOG.trace("Disconnect receiver");
        currentThread.interrupt();
        isOpen = false;
    }

    public void close() {
        LOG.trace("Close receiver");
        try {

            reader.close();
        } catch (IOException e) {
            LOG.error("Receiver reader close exception", e);
        }
    }
}
