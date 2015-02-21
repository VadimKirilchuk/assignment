package ru.assignment.net;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Андрей on 20.02.2015.
 */
public class Sender {
    private Socket clientSocket;
    private PrintWriter writer;
    private Scanner scanner;
    private boolean isOpen=false;

    public Sender(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        writer = new PrintWriter(clientSocket.getOutputStream());
        scanner=new Scanner(System.in);
    }

    public void startSender() {
        isOpen=true;
        try {
            System.out.println("StartSender wait message from console");
                while (scanner.hasNextLine()) {

                    String message = scanner.nextLine()+"\n";
                    System.out.println("sender get message- "+message);
                  //  writer.write(message);
                    //writer.close();
                    try {
                        OutputStream out=clientSocket.getOutputStream();
                        out.write(message.getBytes());
                        out.flush();
                    }catch(IOException e){

                    }
                    if (message.equalsIgnoreCase("disconnect")) {
                        System.out.println("exit sender disconnect");
                        break;
                    }
                }

        } finally {
          closeSender();
        }
    }
    public boolean isOpen(){
        return isOpen;
    }
    public void closeSender(){
        System.out.println("closeSender");
        writer.close();
        scanner.close();
        isOpen=false;


    }
}
