package com.shi.acsserver.service.message.socket;

import java.io.IOException;
import java.util.List;

public interface ISocket {
    void start(String host, int port);
    void stop();    
    void stop(Client client) throws IOException;    
    List<Client> getClientCollection();
}
