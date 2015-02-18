package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ServerThread implements Runnable {
    private int port;
    private ChatModel chatModel;
    private ServerSocket serverSocket;
    int sessionCount;

    public ServerThread(int port) {
        this.port = port;
    }

    public void run() {
        chatModel = new ChatModel();
        try {
            serverSocket = new ServerSocket(port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                startNewSession(socket, sessionCount, chatModel);
            }
        } catch (IOException a) {
            checkChatModel();
            if (!serverSocket.isClosed()) {
                closeSocket(serverSocket);
                a.printStackTrace();
            }
        }
    }

    public void startNewSession(Socket socket, int sessionCount,
                                ChatModel chatModel) {
        Session session = new Session(socket, sessionCount, chatModel);
        chatModel.addNewListener(session);
        Thread sessionThread = new Thread(session);
        sessionThread.start();
        sessionCount++;
    }

    public void closeSocket(ServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkChatModel() {
        if (chatModel != null) {
        } else {
            return;
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