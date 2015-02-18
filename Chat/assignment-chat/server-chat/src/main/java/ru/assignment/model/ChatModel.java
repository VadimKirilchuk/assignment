package ru.assignment.model;

import ru.assignment.ChatMessage;
import ru.assignment.net.Session;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ChatModel {
    private List<Session> listenerList;
    private List<ChatMessage> messageList;
    public ChatModel(){
        listenerList=new ArrayList<>();
        messageList=new LinkedList<>();
    }

    public void addNewMessage(ChatMessage chatMessage){
        messageList.add(chatMessage);

    }
    public void addNewListener(Session listener){
        listenerList.add(listener);

    }
    public void removeListener(int identifier){
        listenerList.remove(identifier);

    }
}
