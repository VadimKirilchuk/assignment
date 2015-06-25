package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Sender {
    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);
    private final Socket clientSocket;
    private final OutputStreamWriter writer;
    private final Scanner scanner;
    private final DisconnectDataListener listener;
    private volatile boolean  isOpen = false;
    private Thread currentThread;


    public Sender(Socket clientSocket, DisconnectDataListener listener) throws IOException {
        LOG.trace("Configuration sender constructor ");
        this.listener=listener;
        this.clientSocket = clientSocket;
        writer = new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_16);
        scanner = new Scanner(System.in);
        currentThread=Thread.currentThread();
    }
    private void sendMessageToServer() throws IOException {
        if (scanner.hasNextLine()) {
            String message = scanner.nextLine() + "\n";
            LOG.info("Sender send message to server, message: {}", message);
            writer.write(message);
            writer.flush();
            if (message.equalsIgnoreCase("disconnect" + "\n")) {
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
        LOG.debug("Sender waiting message from user, sender isOpen= {}", isOpen);
        while (isOpen) {
            try {
                if (System.in.available() == 0) {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException a) {
                        LOG.trace("Interrupt sender at sleep operation");
                        break;
                    }
                } else {
                    sendMessageToServer();
                }
            } catch (IOException e) {
                LOG.error("Sender exception", e);
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
        LOG.trace("Sender disconnect");
        currentThread.interrupt();
        isOpen = false;
    }

    private void close() {
        LOG.trace("Close sender");
        scanner.close();
        try {
            writer.close();
        } catch (IOException e) {
            LOG.error("Sender's writer close exception",e);
        }
    }
}
