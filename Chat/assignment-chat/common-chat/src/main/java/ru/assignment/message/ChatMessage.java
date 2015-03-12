package ru.assignment.message;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ChatMessage {
    private String message;
    public ChatMessage(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }
}
