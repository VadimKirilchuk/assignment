package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ServerClass implements Runnable {
    private final int port;
    private ChatModel chatModel;
    private ServerSocket serverSocket;
    private Set<Session> sessionSet;
    private int sessionCount;

    public ServerClass(int port, ChatModel chatModel) {
        this.port = port;
        this.chatModel = chatModel;
        sessionSet = new HashSet<Session>();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            try {
                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    System.out.println("test");
                    startNewSession(socket, sessionCount, chatModel);
                }
                checkConnections();
            } catch (IOException e) {
                checkConnections();
            }
        } catch (IOException a) {
            closeThread();
            a.printStackTrace();
        }
    }

    public void startNewSession(Socket socket, int sessionCount,
                                ChatModel chatModel) {
        Session session = null;
        try {
            session = new Session(socket, sessionCount, chatModel);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        sessionSet.add(session);
        Thread sessionThread = new Thread(session);
        sessionThread.start();
        sessionCount++;
    }

    public void checkConnections() {
        if (!sessionSet.isEmpty()) {
            clearConnections();
        } else {
            return;
        }
    }

    public void clearConnections() {
        for (Session session : sessionSet) {
            session.closeSession();
        }
    }

    public void closeThread() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}