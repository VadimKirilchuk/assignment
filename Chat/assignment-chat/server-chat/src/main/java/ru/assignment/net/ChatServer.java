package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.util.Scanner;

public class ChatServer {
    private ServerClass serverClass;
    private ChatModel chatModel;
    private ServerConfiguration serverConfiguration;

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
        int port=serverConfiguration.getPort();
        serverClass = new ServerClass(port,chatModel);
        Thread serverSessionThread = new Thread(serverClass);
        serverSessionThread.start();
        waitForCommand(serverSessionThread);
    }

    public void waitForCommand(Thread serverSessionThread) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("For close all sessions enter <quit>");
            while (scanner.hasNextLine()) {
                String consoleCommand = scanner.nextLine();
                if (consoleCommand.equalsIgnoreCase("quit")) {
                    shutDown(serverSessionThread);
                    break;
                } else {
                    System.out.println("Wrong command,try again/for close  please enter <quit>");
                }
            }
            if (serverSessionThread.isAlive()) {
                shutDown(serverSessionThread);
            }
        }
    }

    public void shutDown(Thread serverSessionThread) {
        serverClass.closeThread();
        try {
            serverSessionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}