package ru.assignment.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Андрей on 18.02.2015.
 */
public class ServerConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(ServerConfiguration.class);
    private final int port;

    public ServerConfiguration(int port){
        LOG.trace("Configuration serverConfiguration constructor, port= {}",port);
        this.port=port;
    }

    public int getPort(){
        return port;
    }
}
