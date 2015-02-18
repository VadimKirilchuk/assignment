package exercises;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Андрей on 07.02.2015.
 */
public class BaseSocket {
    public static void main(String[] args) throws IOException {
        //String str=new String(" d g 4 ");
        //System.out.println("f"+str);
        multiServer();
    }

    public static void inetAdress() throws UnknownHostException {
        InetAddress localAddress = InetAddress.getLocalHost();
        System.out.println(localAddress);
        InetAddress address = InetAddress.getByName("www.HerbSchildt.com");
        System.out.println(address);
        InetAddress[] multiAddress = InetAddress.getAllByName("www.nba.com");
        for (InetAddress oneAddress : multiAddress) {
            System.out.println(oneAddress);
        }
        System.out.println(InetAddress.getByName("www.nba.com").isMulticastAddress());
    }

    public static void socketClient() throws IOException {
        Socket socket = new Socket("time-A.timefreq.bldrdoc.gov", 13);
        InputStream stream = socket.getInputStream();
        Scanner scanner = new Scanner(stream);
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
    }

    public static void socketServer() throws IOException {
        try (ServerSocket server = new ServerSocket(8189)) {
            while (true) {
                try (Socket socket = server.accept()) {
                    InputStream input = socket.getInputStream();
                    OutputStream output = socket.getOutputStream();
                    try (Scanner scanner = new Scanner(input)) {
                        PrintWriter writer = new PrintWriter(output, true);
                        writer.println("Hello, press BBB");
                        boolean done = false;
                        while (!done && scanner.hasNextLine()) {
                            String string = scanner.nextLine();
                            writer.println("echo" + string);
                            if (string.trim().equals("BBB")) {
                                //done = true;

                            }
                        }
                    }
                }
            }
        }
    }

    public static void multiServer() throws IOException {
        try (ServerSocket server = new ServerSocket(8189)) {
            int count=1;
            while (true) {
                Socket socket = server.accept();
                System.out.println(count);
                Runnable clientThread=new  WorkWithClientThread(socket);
                Thread thread =new Thread(clientThread);
                thread.start();
                count++;

            }
        }
    }

}
