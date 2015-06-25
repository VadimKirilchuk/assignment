package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.assignment.model.ChatModel;

import java.util.Scanner;

public class ChatServer {
    private static final Logger LOG = LoggerFactory.getLogger(ChatServer.class);
    private final ChatModel chatModel;
    private final ServerConfiguration serverConfiguration;
    private ServerRunnable serverRunnable;



    public static void main(String[] args) {
        LOG.info("Start Main Server");
        ServerConfiguration serverConfiguration = new ServerConfiguration(8185);
        ChatModel chatModel1=new ChatModel();
        ChatServer server = new ChatServer(serverConfiguration,chatModel1);
        server.start();
        LOG.info("Finish Main Server");
    }

    public ChatServer(ServerConfiguration serverConfiguration,ChatModel chatModel) {
        LOG.trace("Configuration Server constructor");
        this.chatModel=chatModel;
        this.serverConfiguration = serverConfiguration;
    }

    public void start() {
        LOG.trace("Start Server");
        int port=serverConfiguration.getPort();
        LOG.trace("Server port= {}", port);
        serverRunnable = new ServerRunnable(port,chatModel);
        Thread serverSessionThread = new Thread(serverRunnable);
        serverSessionThread.start();
        waitForCommand(serverSessionThread);

    }

    public void waitForCommand(Thread serverSessionThread) {
        try (Scanner scanner = new Scanner(System.in)) {
            LOG.trace("Server wating for command");
            while (scanner.hasNextLine()) {
                String consoleCommand = scanner.nextLine();
                LOG.info("User command: {}", consoleCommand);
                if (consoleCommand.equalsIgnoreCase("quit")) {
                    shutDown();
                    break;
                } else {
                    System.out.println("Wrong command,try again/for close  please enter <quit>");
                }
            }
            if (!serverRunnable.isClosed()) {
                shutDown();
            }
        }
    }

    public void shutDown() {
        LOG.trace("Close Server thread");
        serverRunnable.close();

    }
}