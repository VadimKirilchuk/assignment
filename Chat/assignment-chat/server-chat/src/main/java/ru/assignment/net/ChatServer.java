package ru.assignment.net;

import java.util.Scanner;

public class ChatServer {
    private int port;
    private ServerThread serverThread;

    public static void main(String[] args) {
        ServerConfiguration serverConfiguration = new ServerConfiguration(8185);
        ChatServer server = new ChatServer(serverConfiguration);
        server.start();
    }

    public ChatServer(ServerConfiguration serverConfiguration) {
        port = serverConfiguration.getPort();
    }

    public void start() {
        serverThread = new ServerThread(port);
        Thread serverSessionThread = new Thread(serverThread);
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
                if(serverSessionThread.isAlive()){
                    shutDown(serverSessionThread);
                }

        }
    }

    public void shutDown(Thread serverSessionThread) {
        serverThread.closeThread();
        try {
            serverSessionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}