package ru.assignment.net;

import ru.assignment.model.ChatModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ServerRunnable implements Runnable {
    private final int port;
    private ChatModel chatModel;
    private ServerSocket serverSocket;
    private Set<Session> sessionSet;
    private int sessionCount;
    private boolean isClosed = false;

    public ServerRunnable(int port, ChatModel chatModel) {
        this.port = port;
        this.chatModel = chatModel;
        sessionSet = new HashSet<Session>();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10);
            while (!isClosed) {
                try {
                    Socket socket = serverSocket.accept();
                    startNewSession(socket, chatModel);
                } catch (SocketTimeoutException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            System.out.println("close serverRunnable");
            checkConnections();
            shutDown();
        } catch (IOException a) {
            System.out.println("close serverRunnable exception");
            shutDown();
            a.printStackTrace();
        }
    }

    /*
    *
    *  try {

                while (!serverSocket.isClosed()) {

                    Socket socket = serverSocket.accept();

                    startNewSession(socket, chatModel);
                }

                checkConnections();
            } catch (IOException e) {

                checkConnections();
            }
    *
    *
    * */
    public void startNewSession(Socket socket,
                                ChatModel chatModel) {
        Session session = new Session(socket, sessionCount, chatModel);
        sessionSet.add(session);
        session.startAsync();
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
        Iterator<Session> iterator = sessionSet.iterator();
        System.out.println(sessionSet.size());
        while (iterator.hasNext()) {
            Session session = iterator.next();
            iterator.remove();
            System.out.println("close session////");
            session.shutDown();
        }
    }

    public void close() {
        isClosed = true;
    }

    public boolean isClosed(){
        return isClosed;
    }

    public void shutDown() {
        try {
            System.out.println("ServerClass close serversocket");
            serverSocket.close();
            System.out.println("close serverSocket from ServerClass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}