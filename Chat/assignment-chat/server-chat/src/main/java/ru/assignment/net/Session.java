package ru.assignment.net;

import ru.assignment.message.ChatMessage;
import ru.assignment.model.ChatModel;
import ru.assignment.model.ChatModelListener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Session implements Runnable, ChatModelListener {
    private Socket socket;
    private final int sessionIdentifier;
    private ChatModel chatModel;
    private Scanner scanner;
    private PrintWriter writer;

    public Session(Socket socket, int sessionIdentifier,
                   ChatModel chatModel) throws IOException {
        this.socket = socket;
        this.sessionIdentifier = sessionIdentifier;
        this.chatModel = chatModel;
        initConfiguration();
    }

    public void run() {
        System.out.println("Session startSession");
        listen();
        closeSession();
    }

    public void closeSession() {
        System.out.println("closeSession from Session");
        chatModel.removeListener(sessionIdentifier);
        scanner.close();
        writer.close();
        closeSocket();
    }

    public void closeSocket() {
        try {

            System.out.println("close socket");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToChatModel(String message) {
        System.out.println("Session send message to chat model ");
        ChatMessage chatMessage = new ChatMessage(message);
        chatModel.addMessage(chatMessage, this);
    }

    public void initConfiguration() throws IOException {
        try {
            scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            closeSocket();
            throw e;
        }
        chatModel.addListener(this);
    }

    // public void setListenerForChatModel(OutputStream outputStream){

    public void listen() {
        System.out.println("startListenSession wait message");
      /*
        byte[] byt=new byte[16];
        try {
            while (socket.getInputStream().read(byt)!=-1) {

            }
        }catch(IOException e){

        }
        System.out.println("message: "+new String(byt));
        */
        //System.out.println(scanner.nextLine());
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            System.out.println("ListenSession get message -"+message);
            if (message.equalsIgnoreCase("disconnect")) {
                break;
            }
            System.out.println("beforesendMessageToChatModel");
            sendMessageToChatModel(message);
        }
    }

    public void sendMessageToClient(ChatMessage chatMessage) {
        String message = chatMessage.getMessage()+"\n";
        System.out.println("Session send message to client from chat model");
        //System.out.println(message);
        //System.out.println(writer);
        //writer.write(message);
        try {
            OutputStream out=socket.getOutputStream();
            out.write(message.getBytes());
            out.flush();
        }catch(IOException e){}
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (this.getClass() == object.getClass()) {
            Session sessionObject = (Session) object;
            return this.sessionIdentifier == sessionObject.sessionIdentifier;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return sessionIdentifier;
    }
}
