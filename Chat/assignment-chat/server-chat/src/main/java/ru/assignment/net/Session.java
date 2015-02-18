package ru.assignment.net;

import ru.assignment.ChatMessage;
import ru.assignment.model.ChatModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Session implements Runnable {
    private Socket socket;
    private int sessionIdentifier;
    private ChatModel chatModel;
    private Scanner scanner;
    private PrintWriter writer;

    public Session(Socket socket, int sessionIdentifier,
                   ChatModel chatModel) {
        this.socket = socket;
        this.sessionIdentifier = sessionIdentifier;
        this.chatModel = chatModel;
        chatModel.addNewListener(this);
    }

    public void run() {
        initStreams();
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

    public void sentMessageToChatModel(String message) {
        ChatMessage chatMessage = new ChatMessage(message);
        chatModel.addNewMessage(chatMessage);
    }

    public void initStreams() {
        try {
            scanner = new Scanner(socket.getInputStream());
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {

        }
    }

    // public void setListenerForChatModel(OutputStream outputStream){

    public void listen() {
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("disconnect")) {
                break;
            }
            sentMessageToChatModel(message);
        }
    }

    public void sentMessage(ChatMessage chatMessage) {
        String message = chatMessage.getMessage();
        writer.write(message);
    }
}
