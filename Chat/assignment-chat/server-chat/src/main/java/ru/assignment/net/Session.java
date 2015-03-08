package ru.assignment.net;

import ru.assignment.message.ChatMessage;
import ru.assignment.model.ChatModel;
import ru.assignment.model.ChatModelListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Session implements Runnable, ChatModelListener {
    private final Socket socket;
    private final int sessionIdentifier;
    private final ChatModel chatModel;
    private BufferedReader reader;
    private InputStreamReader streamReader;
    private OutputStreamWriter writer;
    private boolean isActive = false;
    private Thread currentThread;

    public Session(Socket socket, int sessionIdentifier,
                   ChatModel chatModel) {
        this.socket = socket;
        this.sessionIdentifier = sessionIdentifier;
        this.chatModel = chatModel;
    }

    public void startAsync() {
        Thread sessionThread = new Thread(this);
        System.out.println("beforestartSession");
        sessionThread.start();
    }

    public void run() {
        System.out.println("Session startSession");
        try {
            init();
            listen();
            closeSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        try {
            currentThread = Thread.currentThread();
            streamReader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16);
            reader = new BufferedReader(streamReader);
            writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_16);
        } catch (IOException e) {
            reader.close();
            writer.close();
            closeSocket();
            throw e;
        }
        chatModel.addListener(this);
    }

    public void shutDown() {
        currentThread.interrupt();
        isActive = false;
    }

    public void closeSession() {
        System.out.println("closeSession from Session"+" identifier "+sessionIdentifier);
        onNewMessage(new ChatMessage("disconnect"));
        chatModel.removeListener(this);
        closeStreams();
        closeSocket();
    }

    public void closeStreams() {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        try {
            System.out.println("close socket");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMessageToChatModel(String message) {
        System.out.println("Session send message to chat model ");
        ChatMessage chatMessage = new ChatMessage(message);
        chatModel.addMessage(chatMessage, this);
    }

    public void listen() {

        /*
        System.out.println("startListenSession wait message");

        while (reader.hasNextLine()) {
            String message = reader.nextLine();
            System.out.println("ListenSession get message -" + message);
            if (message.equalsIgnoreCase("disconnect")) {
                break;
            }
            System.out.println("beforesendMessageToChatModel");
            onMessageToChatModel(message);
        }
*/
        getLastMessages();
        isActive = true;
        while (isActive) {
            try {
                if (!reader.ready()) {
                    try {

                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        break;
                    }
                } else {
                    System.out.println("ReadLine");
                    String message = reader.readLine();
                    if (message.equalsIgnoreCase("disconnect")) {
                        isActive = false;
                    } else {
                        onMessageToChatModel(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("exception");
                e.printStackTrace();
                isActive = false;
            }
        }
        closeSession();
    }

    public void getLastMessages() {
        List<ChatMessage> messageList = chatModel.getLastMessages();
        for (ChatMessage message : messageList) {
            onNewMessage(message);
        }
    }

    public void onNewMessage(ChatMessage chatMessage) {
        String message = chatMessage.getMessage() + "\n";
        System.out.println("Session send message to client from chat model " + message);
        try {
            writer.write(message);
            writer.flush();
            System.out.println("writer finished new message"+ message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
