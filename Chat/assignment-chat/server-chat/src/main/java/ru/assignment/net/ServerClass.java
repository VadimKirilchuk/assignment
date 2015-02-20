package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ServerClass implements Runnable {
    private final int port;
    private  ChatModel chatModel;
    private ServerSocket serverSocket;
    private Map<Integer, Session> sessionMap;
    private int sessionCount;

    public ServerClass(int port,ChatModel chatModel) {
        this.port = port;
        sessionMap = new HashMap();
        this.chatModel=chatModel;
    }

    public void run() {

        try {
            serverSocket = new ServerSocket(port);
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("test");
                startNewSession(socket, sessionCount, chatModel);
            }
        } catch (IOException a) {
            checkConnections();
            if (!serverSocket.isClosed()) {
                closeThread();
            } else {
                a.printStackTrace();
            }
        }
    }

    public void startNewSession(Socket socket, int sessionCount,
                                ChatModel chatModel) throws IOException{
        Session session = new Session(socket, sessionCount, chatModel);
        Thread sessionThread = new Thread(session);
        sessionThread.start();
        sessionCount++;
    }

    public void checkConnections() {
        if (!sessionMap.isEmpty()) {
            clearConnections();
        } else {
            return;
        }
    }

    public void clearConnections() {
        Collection<Session> sessionCollection = sessionMap.values();
        Iterator<Session> iterator = sessionCollection.iterator();
        while (iterator.hasNext()) {
            Session session = iterator.next();
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