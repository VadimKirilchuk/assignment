package ru.assignment.model;

import ru.assignment.message.ChatMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ChatModel {
    private Set<ChatModelListener> listenerSet;
    private List<ChatMessage> messageList;

    public ChatModel() {
        listenerSet = new HashSet<ChatModelListener>();
        messageList = new ArrayList<ChatMessage>();
    }

    public void addMessage(ChatMessage chatMessage,
                           ChatModelListener chatModelListener) {
        //messageSizeList++;
        messageList.add(chatMessage);
        sendMessageToAllListeners(chatMessage, chatModelListener);
    }

    public void addListener(ChatModelListener listener) {
        listenerSet.add(listener);
        sendAllMessagesToNewListener(listener);
    }

    public void removeListener(int identifier) {
        listenerSet.remove(identifier);
    }

    public void sendAllMessagesToNewListener(ChatModelListener listener) {
        int size = messageList.size();
        int startIndex = (Math.min(size, 30) == 30 ? (size - 30) : 0);
        for (int i = startIndex; i < size; i++) {
            listener.sendMessageToClient(messageList.get(i));
        }
    }

    public void sendMessageToAllListeners(ChatMessage chatMessage,
                                          ChatModelListener chatModelListener) {
        for (ChatModelListener listener : listenerSet)
            if (!listener.equals(chatModelListener)) {
                listener.sendMessageToClient(chatMessage);
            }
    }
}
