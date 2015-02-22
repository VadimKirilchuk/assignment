package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
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
                System.out.println("beforeServerClassrun");
                while (!serverSocket.isClosed()) {
                    System.out.println("beforeServerClassaccept");
                    Socket socket = serverSocket.accept();
                    System.out.println("afterServerClassaccept completed");
                    startNewSession(socket, chatModel);
                }
                System.out.println("before close connection after while run() ServerClass");
                checkConnections();
            } catch (IOException e) {
                System.out.println("before close connection after exception run() ServerClass");
                checkConnections();
            }
        } catch (IOException a) {
            System.out.println("exception port ServerClass");
            closeThread();
            a.printStackTrace();
        }
    }

    public void startNewSession(Socket socket,
                                ChatModel chatModel) {
        Session session = null;
        try {
            System.out.println("session count for new listeners " + sessionCount);
            session = new Session(socket, sessionCount, chatModel);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        sessionSet.add(session);
        //System.out.println("start session");
        Thread sessionThread = new Thread(session);
        System.out.println("beforestartSession");
        sessionThread.start();

        sessionCount++;
    }

    public void checkConnections() {
        System.out.println("ServerClass check connection");
        if (!sessionSet.isEmpty()) {
            clearConnections();
        } else {
            return;
        }
    }

    public void clearConnections() {
        System.out.println("clear All sessions ServerClass");
        Iterator<Session> iterator=sessionSet.iterator();
        while (iterator.hasNext()){
            Session session=iterator.next();
            iterator.remove();
            session.closeSession();
        }
    }

    public void closeThread() {
        try {
            System.out.println("ServerClass close serversocket");
            serverSocket.close();
            System.out.println("close serverSocket from ServerClass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}