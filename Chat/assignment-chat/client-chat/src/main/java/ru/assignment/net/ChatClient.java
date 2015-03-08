package ru.assignment.net;

import java.io.IOException;
import java.net.Socket;

public class ChatClient implements DisconnectReceivedListener {
    private final ClientConfiguration clientConfiguration;
    private Sender sender;
    private Receiver receiver;
    private Socket clientSocket;

    public static void main(String[] ar) {
        ClientConfiguration clientConfiguration = new ClientConfiguration(8185, "localhost");
        ChatClient chatClient = new ChatClient(clientConfiguration);
        chatClient.startClient();
    }

    public ChatClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public void startClient() {
        try {
            init();
            System.out.println("beforestartSession");
            startSession();
            System.out.println("beforefinishSession");
            finishSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        int serverPort = clientConfiguration.getServerPort();
        String serverHost = clientConfiguration.getServerHost();
        clientSocket = new Socket(serverHost, serverPort);
        sender = new Sender(clientSocket, this);
        receiver = new Receiver(clientSocket, this);
    }

    public void startSession() {
        System.out.println("startSession");
        receiver.connectAsync();
        sender.connect();
    }

    public void finishSession() throws IOException {
        System.out.println("finishSession");
        clientSocket.close();
    }

    @Override
    public void disconnectReceived() {
        if (sender.isOpen()) {
            receiver.disconnect();
        }
        if (sender.isOpen()) {
            sender.disconnect();
        }
    }
}