package com.shi.acsserver.service.message.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketServer implements ISocket{       

    ServerSocket serverSocket;   
        
    List<Client> clientCollection = new Vector<Client>();

    ExecutorService executorService;

    @Override
    public void start(String host, int port){

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host, port));
        } catch (Exception e) {     
            if(!serverSocket.isClosed()){
                stop();
            }
            return;
        }

        Runnable runnable = new Runnable(){

            @Override
            public void run() {

                log.info("[서버시작]");     

                while(true){
                    try {
                        Socket socket = serverSocket.accept();
                        String message = "[연결수락: " +  socket.getRemoteSocketAddress() + " : " + Thread.currentThread() + "]";
                        log.info(message);
                                                                                                            
                        clientCollection.add(new Client(socket));
                        
                        log.info("[연결 개수 : " + clientCollection.size() + "]");

                    } catch (IOException e) {
                        if(!serverSocket.isClosed()){
                            stop();
                        }
                        break;
                    }
                }    

            }

        };
        executorService.submit(runnable);
    }

    @Override
    public void stop(){
        try {
            Iterator<Client> iterator = clientCollection.iterator();
            while(iterator.hasNext()){
                Client client = iterator.next();
                stop(client);                
                iterator.remove();                            
            }               
            if(serverSocket !=null && !serverSocket.isClosed()){
                serverSocket.close();
            }
            if(executorService !=null && !executorService.isShutdown()){
                executorService.shutdown();
            }
            log.info("[종료 Process Complete!]");
        } catch (Exception e) {            

        }     
    }
   
    @Override
    public void stop(Client client) throws IOException {
            client.socket.close();
            clientCollection.remove(client);
            log.info("[" + client.socket.getRemoteSocketAddress() + " : 종료 : 남은 Collection : " + clientCollection.size() + " ]" );
    }

    @Override
    public List<Client> getClientCollection(){
        return clientCollection;
    }
}

