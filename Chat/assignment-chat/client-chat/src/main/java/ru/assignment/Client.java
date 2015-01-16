package ru.assignment;
/**
 * Created by Андрей on 16.01.2015.
 */
import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] ar) {
        int serverPort = 6666;
        String address = "127.0.0.1";


        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            System.out.println("socket with IP address " + address + " and port " + serverPort + "?");
            Socket socket = new Socket(ipAddress, serverPort);



            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();


            DataInputStream in = new DataInputStream(sin);
            DataOutputStream out = new DataOutputStream(sout);


            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            System.out.println("Type in something and press enter.");
            System.out.println();

            while (true) {
                line = keyboard.readLine();
                System.out.println("Sending this line to the server...");
                out.writeUTF(line);
                out.flush();
                line = in.readUTF();
                System.out.println("The server  sent  this : " + line);
                System.out.println("Looks great");
                System.out.println();
            }
        } catch (IOException x) {
            x.printStackTrace();
        }
    }
}