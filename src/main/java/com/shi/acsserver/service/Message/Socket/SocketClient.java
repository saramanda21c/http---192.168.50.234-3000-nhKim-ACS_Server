package com.shi.acsserver.service.Message.Socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class SocketClient implements ISocket{
    
    ExecutorService executorService;    
    
    List<Client> clientCollection = new Vector<Client>();    

    @Override
    public void start(String hostname, int port){

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());        
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                Socket socket = new Socket();
                try {
                    
                    log.info("[연결요청]");

                    socket.connect(new InetSocketAddress(hostname, port));                    
                    clientCollection.add(new Client(socket));

                    log.info("[연결성공 : Server IP:" + socket.getRemoteSocketAddress() + " : " + Thread.currentThread());

                } catch (Exception e) {
                    log.info("[연결거부 : " + e.getMessage());                                   
                    if(!socket.isClosed()){
                        try {
                            socket.close();
                        } catch (IOException ioe) {                        
                            ioe.printStackTrace();
                        }                 
                    }
                }                
                
            }

        };

        executorService.submit(runnable);
    }

    @Override
    public void stop(Client client) throws IOException{        
        client.socket.close();        
        clientCollection.remove(client);
        log.info("[" + client.socket.getRemoteSocketAddress() + " : 종료 : 남은 Collection : " + clientCollection.size() + " ]" );
    }

    @Override
    public void stop() {        
        try {
            Iterator<Client> iterator = clientCollection.iterator();
            while(iterator.hasNext()){
                Client client = iterator.next();
                stop(client);                
                iterator.remove();                            
            }               

            if(executorService !=null && !executorService.isShutdown()){
                executorService.shutdown();
            }
            log.info("[종료 Process Complete!]");
        } catch (Exception e) {            

        }  
    }

    @Override
    public List<Client> getClientCollection(){
        return clientCollection;
    }

}
