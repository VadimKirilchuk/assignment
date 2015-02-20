package ru.assignment.net;



import java.io.IOException;
import java.net.Socket;


public class ChatClient {
    private ClientConfiguration clientConfiguration;
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
            initConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        startSession();
        finishSession();

    }

    public void initConfiguration() throws IOException {
        int serverPort = clientConfiguration.getServerPort();
        String serverHost = clientConfiguration.getServerHost();
        clientSocket = new Socket(serverHost, serverPort);
        sender = new Sender(clientSocket);
        receiver = new Receiver(clientSocket, sender);
    }

    public void startSession() {
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
        sender.startSender();

    }



    public void finishSession() {
        try {
            clientSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}