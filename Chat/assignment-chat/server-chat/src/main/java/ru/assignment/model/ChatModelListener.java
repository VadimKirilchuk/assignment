package ru.assignment.model;

import ru.assignment.message.ChatMessage;

/**
 * Created by Андрей on 19.02.2015.
 */
public interface ChatModelListener {
    void onNewMessage(ChatMessage chatMessage);
}
