package exercises.Network;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by Андрей on 07.02.2015.
 */
public class BaseSocket {
    private int f;

    public static void main(String[] args) {

    }

    public static void show() throws IOException {
        try (FileInputStream str = new FileInputStream("e:\\hhhds")) {

        }
    }

    public BaseSocket() {
        //this.f=5;
    }

    public void client() throws IOException {

        Socket socket = new Socket("localhost", 8183);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        int i = 10;
        while (i > 0) {
            writer.write("test");
            i--;
        }
        socket.close();
    }

    public void server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8183);
        Socket socket = serverSocket.accept();
        Scanner scanner = new Scanner(socket.getInputStream());
        while (scanner.hasNextLine()) {
            System.out.println(scanner.nextLine());
        }
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
            int count = 1;
            while (true) {
                Socket socket = server.accept();
                System.out.println(count);
                Runnable clientThread = new WorkWithClientThread(socket);
                Thread thread = new Thread(clientThread);
                thread.start();
                count++;
            }
        }
    }
}

