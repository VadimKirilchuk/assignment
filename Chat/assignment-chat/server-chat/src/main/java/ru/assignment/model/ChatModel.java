package ru.assignment.model;

import ru.assignment.message.ChatMessage;
import ru.assignment.net.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ChatModel {
    private final Set<ChatModelListener> listenerSet;
    private final List<ChatMessage> messageList;

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

    public void removeListener(Session listener) {
        System.out.println("remove Session from ChatModel");
        listenerSet.remove(listener);
    }

    public List<ChatMessage> getLastMessages() {
        System.out.println("send All messages to New Listener from ChatModel");
        int size = messageList.size();
        int startIndex = (Math.min(size, 30) == 30 ? (size - 30) : 0);
        return messageList.subList(startIndex,size);
    }

    public void sendMessageToAllListeners(ChatMessage chatMessage,
                                          ChatModelListener chatModelListener) {
        System.out.println("ChatModel send message to All "+"listenerset size-"+listenerSet.size());
        for (ChatModelListener listener : listenerSet){
            System.out.println(listener.hashCode()+" listener hashcode");
            if (!listener.equals(chatModelListener)) {


                    listener.onNewMessage(chatMessage);

            }
        }
    }
}
