package ru.assignment.net;

/**
 * Created by Андрей on 20.02.2015.
 */
public class ClientConfiguration {
    private final String  serverHost;
    private final int serverPort;

    public ClientConfiguration(int serverPort, String serverHost) {
        this.serverPort = serverPort;
        this.serverHost=serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }
    public String getServerHost(){
        return serverHost;
    }
}
