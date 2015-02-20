package ru.assignment.model;

import ru.assignment.ChatMessage;

/**
 * Created by Андрей on 19.02.2015.
 */
public interface ChatModelListener {
    void sendMessageToClient(ChatMessage chatMessage);
    void sendMessageToChatModel(String message);

}
