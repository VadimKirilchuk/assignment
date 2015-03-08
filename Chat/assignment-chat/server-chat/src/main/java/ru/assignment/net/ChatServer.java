package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.util.Scanner;

public class ChatServer {
    private ServerRunnable serverRunnable;
    private final ChatModel chatModel;
    private final ServerConfiguration serverConfiguration;

    public static void main(String[] args) {
        ServerConfiguration serverConfiguration = new ServerConfiguration(8185);
        ChatModel chatModel1=new ChatModel();
        ChatServer server = new ChatServer(serverConfiguration,chatModel1);
        server.start();
    }

    public ChatServer(ServerConfiguration serverConfiguration,ChatModel chatModel) {
        this.chatModel=chatModel;
        this.serverConfiguration = serverConfiguration;
    }

    public void start() {
        System.out.println("startServer");
        int port=serverConfiguration.getPort();
        serverRunnable = new ServerRunnable(port,chatModel);
        Thread serverSessionThread = new Thread(serverRunnable);
        serverSessionThread.start();
        waitForCommand(serverSessionThread);
    }

    public void waitForCommand(Thread serverSessionThread) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("For close all sessions enter <quit>");
            while (scanner.hasNextLine()) {
                String consoleCommand = scanner.nextLine();
                if (consoleCommand.equalsIgnoreCase("quit")) {
                    shutDown();
                    break;
                } else {
                    System.out.println("Wrong command,try again/for close  please enter <quit>");
                }
            }
            if (serverRunnable.isClosed()) {

                shutDown();
            }
        }
    }

    public void shutDown() {
        System.out.println("close ServerClass thread");
        serverRunnable.close();

    }
}