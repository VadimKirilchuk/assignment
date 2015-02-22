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
        System.out.println("add messagetoChatModel");
        messageList.add(chatMessage);
        sendMessageToAllListeners(chatMessage, chatModelListener);
    }

    public void addListener(ChatModelListener listener) {
        System.out.println("ChatModel add new listener");
        listenerSet.add(listener);
        System.out.println("model set sum "+listenerSet.size());
    }

    public void removeListener(int identifier) {
        System.out.println("remove Session from ChatModel");
        listenerSet.remove(identifier);
    }

    public void sendAllMessagesToNewListener(ChatModelListener listener) {
        System.out.println("send All messages to New Listener from ChatModel");
        int size = messageList.size();
        int startIndex = (Math.min(size, 30) == 30 ? (size - 30) : 0);
        for (int i = startIndex; i < size; i++) {
            listener.sendMessageToClient(messageList.get(i));
        }
    }

    public void sendMessageToAllListeners(ChatMessage chatMessage,
                                          ChatModelListener chatModelListener) {
        System.out.println("ChatModel send message to All "+"listenerset size-"+listenerSet.size());
        for (ChatModelListener listener : listenerSet){
            System.out.println(listener.hashCode()+" listener hashcode");
            if (!listener.equals(chatModelListener)) {
                listener.sendMessageToClient(chatMessage);
            }
        }
    }
}
