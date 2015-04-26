package ru.assignment.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class ChatClient implements DisconnectDataListener {
    private static final Logger LOG =LoggerFactory.getLogger(ChatClient.class);
    private final ClientConfiguration clientConfiguration;
    private Sender sender;
    private Receiver receiver;
    private Socket clientSocket;


    public static void main(String[] ar) {

        LOG.info("Main start");
        ClientConfiguration clientConfiguration = new ClientConfiguration(8185, "localhost");
        ChatClient chatClient = new ChatClient(clientConfiguration);
        chatClient.startClient();
        LOG.info("Main finish");
    }

    public ChatClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public void startClient() {
        try {
            LOG.trace("Start Client");
            init();
            startSession();
            finishSession();
            LOG.trace("Finish Client");
        } catch (IOException e) {
            LOG.error("StartClient exception", e);
        }
    }

    public void init() throws IOException {
        LOG.trace("Init parameters");
        int serverPort = clientConfiguration.getServerPort();
        LOG.debug("ServerPort= {}", serverPort);
        String serverHost = clientConfiguration.getServerHost();
        LOG.debug("ServerHost={}", serverHost);
        clientSocket = new Socket(serverHost, serverPort);
        sender = new Sender(clientSocket, this);
        receiver = new Receiver(clientSocket, this);
        LOG.trace("Finish init");
    }

    public void startSession() {
        LOG.trace("Start session");
        receiver.connectAsync();
        sender.connect();

    }

    public void finishSession() throws IOException {
        clientSocket.close();
        LOG.trace("Finish  session");
    }

    @Override
    public synchronized void finishDataOperation() {
        LOG.trace("Start disconectReceived");
        if (receiver.isOpen()) {
            receiver.disconnect();
        }
        LOG.trace("Receiver condition= {}", receiver.isOpen());
        if (sender.isOpen()) {
            sender.disconnect();
        }
        LOG.trace("Sender condition= {}", sender.isOpen());
    }
}