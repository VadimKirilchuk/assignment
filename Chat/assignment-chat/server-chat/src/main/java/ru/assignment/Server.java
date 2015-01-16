package ru.assignment;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Андрей on 16.01.2015.
 */
public class Server {
    public static void main(String[] ar) {
        int port = 6666;
        try {
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Waiting for a client...");

            Socket socket = ss.accept();
            System.out.println("Got a client ");
            System.out.println();


            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();


            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);

            String line = null;
            while (true) {
                line = in.readUTF();
                System.out.println("client  sent  this line : " + line);
                System.out.println(" sending it back...");
                out.writeUTF(line);
                out.flush();

                            }
        } catch (IOException x) {
            x.printStackTrace();
        }
    }
}