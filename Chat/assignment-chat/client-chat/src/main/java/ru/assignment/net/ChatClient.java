package ru.assignment.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class ChatClient implements DisconnectReceivedListener {
    private final ClientConfiguration clientConfiguration;
    private Sender sender;
    private Receiver receiver;
    private Socket clientSocket;
    public static Logger clientLogger ;

    public static void main(String[] ar) {
        clientLogger=LoggerFactory.getLogger(ChatClient.class);
        clientLogger.debug("Main method start");
        ClientConfiguration clientConfiguration = new ClientConfiguration(8185, "localhost");
        ChatClient chatClient = new ChatClient(clientConfiguration);
        chatClient.startClient();
    }

    public ChatClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public void startClient() {
        try {
            clientLogger.debug("Start Client");
            init();
            startSession();
            finishSession();
            clientLogger.debug("Finish Start Client");
        } catch (IOException e) {
            clientLogger.debug("StartClient exception", e);
        }
    }

    public void init() throws IOException {
        clientLogger.debug("Init parameters");
        int serverPort = clientConfiguration.getServerPort();
        clientLogger.debug("ServerPort- {}", serverPort);
        String serverHost = clientConfiguration.getServerHost();
        clientLogger.debug("ServerHost- {}", serverHost);
        clientSocket = new Socket(serverHost, serverPort);
        sender = new Sender(clientSocket, this);
        receiver = new Receiver(clientSocket, this);
        clientLogger.debug("Finish init");
    }

    public void startSession() {
        clientLogger.debug("Start session");
        receiver.connectAsync();
        sender.connect();
        clientLogger.debug("Finish start session");
    }

    public void finishSession() throws IOException {
        clientLogger.debug("Start finish session");
        clientSocket.close();
        clientLogger.debug("Finish finish session");
    }

    @Override
    public void disconnectReceived() {
        clientLogger.debug("Start disconectReceived");
        if (receiver.isOpen()) {
            receiver.disconnect();
        }
        clientLogger.debug("Receiver condition- {}", receiver.isOpen());
        if (sender.isOpen()) {
            sender.disconnect();
        }
        clientLogger.debug("Sender condition- {}", sender.isOpen());
    }
}