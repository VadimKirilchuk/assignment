package ru.assignment.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.assignment.message.ChatMessage;
import ru.assignment.net.Session;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ChatModel {
    private static final Logger LOG = LoggerFactory.getLogger(ChatModel.class);
    private final Set<ChatModelListener> listenerSet;
    private final List<ChatMessage> messageList;


    public ChatModel() {
        LOG.trace("Configuration ChatModel constructor");
        listenerSet = new HashSet<ChatModelListener>();
        messageList = new ArrayList<ChatMessage>();
    }

    public synchronized void addMessage(ChatMessage chatMessage,
                           ChatModelListener chatModelListener) {
        LOG.trace("Add message to ChatModel {}",chatMessage.getMessage());

        messageList.add(chatMessage);
        sendMessageToAllListeners(chatMessage, chatModelListener);
    }

    public synchronized void addListener(ChatModelListener listener) {
        LOG.trace("Add listener to ChatModel");
        listenerSet.add(listener);

    }

    public synchronized void removeListener(Session listener) {
        LOG.trace("Remove listener from ChatModel");
        listenerSet.remove(listener);
    }

    public synchronized List<ChatMessage> getLastMessages() {
        LOG.trace("Receive message from ChatModel");
        int size = messageList.size();
        int startIndex = (Math.min(size, 30) == 30 ? (size - 30) : 0);
        return messageList.subList(startIndex,size);
    }

    public void sendMessageToAllListeners(ChatMessage chatMessage,
                                          ChatModelListener chatModelListener) {
        LOG.trace("Send message to All listeners");
        for (ChatModelListener listener : listenerSet){
            if (!listener.equals(chatModelListener)) {
                    listener.onNewMessage(chatMessage);

            }
        }
    }
}
