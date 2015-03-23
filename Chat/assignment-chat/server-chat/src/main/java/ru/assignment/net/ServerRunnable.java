package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(ServerRunnable.class);
    private final int port;
    private ChatModel chatModel;
    private ServerSocket serverSocket;
    private Set<Session> sessionSet;
    private int sessionCount;
    private boolean isClosed = false;


    public ServerRunnable(int port, ChatModel chatModel) {
        LOG.trace("Configuration ServerRunnable constructor, port= {}",port);
        this.port = port;
        this.chatModel = chatModel;
        sessionSet = new HashSet<Session>();
    }

    public void run() {
        try {
            LOG.debug("Run serverRunnable");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(10);
            while (!isClosed) {
                try {
                    Socket socket = serverSocket.accept();
                    LOG.trace("Accept connection");
                    startNewSession(socket, chatModel);
                } catch (SocketTimeoutException e) {

                } catch (IOException e) {
                    LOG.error("ServerRunnable accept exception", e);
                    break;
                }
            }

            checkConnections();
            shutDown();
        } catch (IOException a) {
            LOG.error("ServerRunnable server soccet exception", a);
            shutDown();

        }
    }


    public void startNewSession(Socket socket,
                                ChatModel chatModel) {
        LOG.trace("Start new Session number= {}", sessionCount);
        Session session = new Session(socket, sessionCount, chatModel);
        sessionSet.add(session);
        session.startAsync();
        sessionCount++;
    }

    public void checkConnections() {

        LOG.trace("ServerRunnable check connection");
        if (!sessionSet.isEmpty()) {
            clearConnections();
        } else {
            return;
        }
    }

    public void clearConnections() {
        LOG.trace("Clear all connections");
        Iterator<Session> iterator = sessionSet.iterator();
        System.out.println(sessionSet.size());
        while (iterator.hasNext()) {
            Session session = iterator.next();
            iterator.remove();
            session.shutDown();
        }
    }

    public void close() {
        LOG.trace("Close ServerRunnable , isClosed= {}",isClosed);
        isClosed = true;
    }

    public boolean isClosed(){
        return isClosed;
    }

    public void shutDown() {
        try {
            LOG.trace("ShutDown ServerRunnable");
            serverSocket.close();

        } catch (IOException e) {
            LOG.error("ServerSocket close exception",e);
        }
    }
}