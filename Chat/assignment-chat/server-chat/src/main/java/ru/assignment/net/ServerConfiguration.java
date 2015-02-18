package ru.assignment.net;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ServerConfiguration {
    private int port;
    public ServerConfiguration(int port){
        this.port=port;
    }

    public int getPort(){
        return port;
    }
}
