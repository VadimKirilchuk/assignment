package exercises;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static void main(String[] args) throws IOException {

Logger log=LoggerFactory.getLogger("ROOT");

        log.debug("test");








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

    public static void test(A obj) {
        obj.show();
    }
}

class A {
    int s = 1;

    public void show() {
        System.out.println(s);
    }
}

class B extends A {
    int s = 2;

    public void show() {
        System.out.println(s);
    }
}

/*
public class Main {

    public static void main(String args[])  {
        Collection c = new HashSet();
        Main et = new Main();
        et.sort(coffee);
        et.sort(new HashSet());
    }

    //overloaded method takes Collection argument
    public Collection sort(Collection c){
        System.out.println("Inside Collection sort method");
        return c;
    }


    //another overloaded method which takes HashSet argument which is sub class
    public Collection sort(HashSet hs){
        System.out.println("Inside HashSet sort method");
        return hs;
    }

}



javac Main.java
        [19:53:44] Vadim Kirilchuk: javap -verbose Main.class
[19:54:01] Vadim Kirilchuk: public static void main(java.lang.String[]);
        descriptor: ([Ljava/lang/String;)V
        flags: ACC_PUBLIC, ACC_STATIC
        Code:
        stack=3, locals=3, args_size=1
        0: new           #2                  // class java/util/HashSet
        3: dup
        4: invokespecial #3                  // Method java/util/HashSet."<init>":()V
        7: astore_1
        8: new           #4                  // class Main
        11: dup
        12: invokespecial #5                  // Method "<init>":()V
        15: astore_2
        16: aload_2
        17: aload_1
        18: invokevirtual #6                  // Method sort:(Ljava/util/Collection;)Ljava/util/Collection;
        21: pop
        22: aload_2
        23: new           #2                  // class java/util/HashSet
        26: dup
        27: invokespecial #3                  // Method java/util/HashSet."<init>":()V
        30: invokevirtual #7                  // Method sort:(Ljava/util/HashSet;)Ljava/util/Collection;
        33: pop
        34: return


        */
interface Z<E> {
}

class AA<E> implements Z<E> {
    int val;

    AA() {

    }

    public AA dow() {
        return this;
    }
}
