package ru.assignment.model;

import ru.assignment.ChatMessage;
import ru.assignment.net.Session;

import java.util.*;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ChatModel {
    private Map<Integer, Session> listenerMap;
    private List<ChatMessage> messageList;
    private int messageSizeList;

    public ChatModel(Map listenerMap) {
        this.listenerMap = listenerMap;
        messageList = new ArrayList<>();
    }

    public void addMessage(ChatMessage chatMessage, int sessionIdentifier) {
        messageSizeList++;
        messageList.add(chatMessage);
        sendMessageToAllListeners(chatMessage, sessionIdentifier);
    }

    public void addListener(Integer identifier, Session listener) {
        System.out.println(listener.writer);
        listenerMap.put(identifier, listener);
        sendAllMessagesToNewListener(listener);
    }

    public void removeListener(int identifier) {
        listenerMap.remove(identifier);
    }

    public void sendAllMessagesToNewListener(Session listener) {
        if (messageSizeList > 0) {
            int startPoint;
            if (messageSizeList > 50) {
                startPoint = messageSizeList - 50;
            } else {
                startPoint = 0;
            }
            for (int i = startPoint; i < messageSizeList; i++) {
                listener.sentMessageToClient(messageList.get(i));
            }
        }
    }

    public void sendMessageToAllListeners(ChatMessage chatMessage, int sessionIdentifier) {
        Set<Map.Entry<Integer, Session>> entrySet = listenerMap.entrySet();
        Iterator<Map.Entry<Integer, Session>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Session> entry = iterator.next();
            if (entry.getKey() != sessionIdentifier) {
                entry.getValue().sentMessageToClient(chatMessage);
            }
        }
    }
}
