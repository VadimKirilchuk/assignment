package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Андрей on 20.02.2015.
 */
public class ClientConfiguration {
    private final String serverHost;
    private final int serverPort;
    private static final Logger LOG = LoggerFactory.getLogger(ClientConfiguration.class);

    public ClientConfiguration(int serverPort, String serverHost) {
        LOG.trace("Constructor configuration: serverPort= {}, serverHost= {}",
                                      serverPort, serverHost);
        this.serverPort = serverPort;
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }
}
