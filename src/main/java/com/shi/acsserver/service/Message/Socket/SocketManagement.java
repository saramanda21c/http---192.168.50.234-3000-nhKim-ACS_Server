package com.shi.acsserver.service.message.socket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketManagement implements CommandLineRunner{
    
    String socketType = "Client";
    ISocket socket;
    
    Map<String, Integer> hostCollection = new HashMap<String, Integer>();     

    @Override
    public void run(String... args) throws Exception {
        
        //get Database
        //hostCollection.put("0.0.0.0", 6001);
        hostCollection.put("192.168.50.174", 9990);
        hostCollection.put("192.168.50.167", 12000);
        switch (socketType) {
            case "Server":                
                socket = new SocketServer();            
                break;
        
            default:
                socket = new SocketClient();
                break;
        }

        log.info("[Socket Type is " + socketType + " ]");

        hostCollection.forEach((key, value)->{            
                socket.start(key, value);
        });      
    }



}
