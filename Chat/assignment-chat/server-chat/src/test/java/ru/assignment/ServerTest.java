package ru.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.assignment.model.ChatModel;
import ru.assignment.net.ChatServer;
import ru.assignment.net.ServerRunnable;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;

/**
 * Created by Андрей on 22.03.2015.
 */
@RunWith(JUnitParamsRunner.class)
public class ServerTest {
    private static ChatServer server;
    private static ServerRunnable serverRunnable;
    private volatile int messageCount = 0;
    private int socketNumber = 8185;
    private List<Client> clientList;

    @BeforeClass
    public static void init() {

        serverRunnable = new ServerRunnable(8185, new ChatModel());
        Thread serverSessionThread = new Thread(serverRunnable);
        serverSessionThread.start();
    }

    private class Client extends Thread {
        String message;
        int socketNumber;
        List<String> list;
        BufferedReader reader;
        BufferedWriter writer;
        Socket socket;

        Client(String message, int socketNumber) {
            try {
                this.message = message;
                list = new ArrayList();
                list.add(message);
                this.socketNumber = socketNumber;
                socket = new Socket("localhost", socketNumber);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16));
            } catch (IOException e) {
                fail(e.toString());
            }
        }

        public void run() {
            try {

                try {
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                    ServerTest.this.messageCount++;
                    String messageFromServer;
                    while (!(messageFromServer = reader.readLine()).equals("disconnect")){
                        list.add(messageFromServer);}
                } catch (IOException e) {
                    fail(e.toString());
                }
            } finally {
                close();
            }
        }

        public List<String> getMessageList() {
            return list;
        }

        public int getMessageCount() {
            return list.size();
        }

        public String getMessage() {
            return message;
        }

        public void close() {
            try {
                writer.close();
                reader.close();
                socket.close();
            } catch (IOException e) {
                fail(e.toString());
            }
        }
    }

    @Before
    public void generateClients() {
        clientList = new ArrayList<>();
    }

    @Test
    @Parameters
    public void testConnectToServer(int count) {
        int i = 1;

        while (i <= count) {
            Client client = new Client(String.valueOf(i), socketNumber);
            clientList.add(client);

            i++;
        }

        for (Client client : clientList) {
            client.start();
        }

        while (messageCount < count) ;

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            fail();
        }
        for (Client client : clientList) {
            assertTrue("client mesage count " + client.getMessageCount() +
                    " count " + count, client.getMessageCount() == count);
            int first = 0;
            int second = 0;
            Iterator<String> iterator = client.getMessageList().iterator();
            assertEquals(iterator.next(), client.getMessage());
            if (iterator.hasNext()) {
                first = Integer.valueOf(iterator.next());
            }
            while (iterator.hasNext()) {
                second = Integer.valueOf(iterator.next());
                assertTrue("illegal order " + "first-" + first + " second-" + second, first < second);

            }
        }
    }

    private Object[] parametersForTestConnectToServer() {
        return $(3, 5, 10, 20);
    }

     @AfterClass
      public static  void closeServer() {
       serverRunnable.close();
      }
}
