package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(Session.class);

    public Session(Socket socket, int sessionIdentifier,
                   ChatModel chatModel) {
        LOG.trace("Configuration Server constructor");
        this.socket = socket;
        this.sessionIdentifier = sessionIdentifier;
        this.chatModel = chatModel;
    }

    public void startAsync() {
        LOG.trace("Session start async");
        Thread sessionThread = new Thread(this);
        sessionThread.start();
    }

    public void run() {
        LOG.info("Run session {}", sessionIdentifier);
        try {
            init();
            listen();
            closeSession();
        } catch (IOException e) {
            LOG.error("Session run exception", e);
        }
    }

    public void init() throws IOException {
        try {
            LOG.trace("Session init");
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
        LOG.trace("Shutdown session");
        currentThread.interrupt();
        isActive = false;
    }

    public void closeSession() {
        LOG.trace("Close session {}", sessionIdentifier);
        onNewMessage(new ChatMessage("disconnect"));
        chatModel.removeListener(this);
        closeStreams();
        closeSocket();
    }

    public void closeStreams() {
        try {
            LOG.trace("Close streams");
            reader.close();
            writer.close();
        } catch (IOException e) {
            LOG.error("Close streams exception", e);
        }
    }

    public void closeSocket() {
        try {
            LOG.trace("Close socket");
            socket.close();
        } catch (IOException e) {
            LOG.error("Close socket exception", e);
        }
    }

    public void onMessageToChatModel(String message) {
        LOG.info("Sent message to ChatModel, message= {}", message);
        ChatMessage chatMessage = new ChatMessage(message);
        chatModel.addMessage(chatMessage, this);
    }

    public void listen() {

        LOG.debug("Session listen start");
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
                    String message = reader.readLine();
                    LOG.info("Get message from client",message);
                    if (message.equalsIgnoreCase("disconnect")) {
                        isActive = false;
                    } else {
                        onMessageToChatModel(message);
                    }
                }
            } catch (IOException e) {
                LOG.info("Session listen exception", e);
                isActive = false;
            }
        }

    }

    public void getLastMessages() {
        LOG.trace("Get last messages from ChatModel");
        List<ChatMessage> messageList = chatModel.getLastMessages();
        for (ChatMessage message : messageList) {
            onNewMessage(message);
        }
    }

    public void onNewMessage(ChatMessage chatMessage) {
        String message = chatMessage.getMessage() + "\n";
        LOG.info("Send message from server to client, message= {}",message);
        try {
            writer.write(message);
            writer.flush();

        } catch (IOException e) {
            LOG.error("Server-Client exception",e);
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
