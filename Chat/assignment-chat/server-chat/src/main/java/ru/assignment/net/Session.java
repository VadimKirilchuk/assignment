package ru.assignment.net;

import ru.assignment.ChatMessage;
import ru.assignment.model.ChatModel;
import ru.assignment.model.ChatModelListener;

import java.io.IOException;
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
        listen();
        closeSession();
    }

    public void closeSession() {
        chatModel.removeListener(sessionIdentifier);
        scanner.close();
        writer.close();
        closeSocket();
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToChatModel(String message) {
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
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("disconnect")) {
                break;
            }
            sendMessageToChatModel(message);
        }
    }

    public void sendMessageToClient(ChatMessage chatMessage) {
        String message = chatMessage.getMessage();
        //System.out.println(message);
        //System.out.println(writer);
        writer.write(message);
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
