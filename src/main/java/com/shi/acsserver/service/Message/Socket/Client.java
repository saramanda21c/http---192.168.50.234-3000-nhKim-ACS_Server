package com.shi.acsserver.service.message.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.shi.acsserver.config.RabbitMqConfig;
import com.shi.acsserver.service.message.IMessageProducer;
import com.shi.acsserver.service.message.rabbit.MessageProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client{

    Socket socket;            
    ExecutorService executorService;  
    RabbitMqConfig rabbitMqConfig = new RabbitMqConfig();  
    IMessageProducer producer;

    public Client(Socket socket) throws SocketException {
        this.socket = socket;              
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());                  
        producer = new MessageProducer(rabbitMqConfig.rabbitTemplate());
        receive();       
    }

    public void receive(){
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                try {
                    while (true) {
                        byte[] byteArray = new byte[100];                        
                        InputStream inputStream = socket.getInputStream();

                        int readByteCount = inputStream.read(byteArray);                          

                        if(readByteCount == -1){
                            throw new IOException();
                        }
                        
                        String sysMessage = "[Message : " + socket.getRemoteSocketAddress() + " : " + Thread.currentThread().getName() +"]";
                        log.info(sysMessage);

                        String dataMessage = new String(byteArray, 0, readByteCount, "UTF-8");                        
                        log.info(dataMessage);

                        send(dataMessage);  

                        //producer switch - data Select Info AGV                         
                        producer.sendMessage(dataMessage);
                    }                       
                } catch (Exception e) {
                    //clientCollection.remove(Client.this);
                    String exceptionMessage = "[??????????????? ?????? ?????? : " + socket.getRemoteSocketAddress() + " : " + Thread.currentThread().getName() +"]";
                    log.info(exceptionMessage);
                    log.info(e.getMessage().toString());
                    
                    try {
                        socket.close();
                    } catch (IOException e1) {                        
                        e1.printStackTrace();
                    }
                }
             
            }            

        };

        executorService.submit(runnable);
    }

    public void send(String message){
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                try {

                    byte[] byteArray = message.getBytes("UTF-8");
                    OutputStream outputStream = socket.getOutputStream();                    
                    outputStream.write(byteArray);                    
                    outputStream.flush();

                } catch (Exception e) {                    
                    //clientCollection.remove(Client.this);
                    String exceptionMessage = "[??????????????? ?????? ?????? : " + socket.getRemoteSocketAddress() + " : " + Thread.currentThread().getName() +"]";
                    log.info(exceptionMessage);
                    
                    try {
                        socket.close();
                    } catch (IOException e1) {                        
                        e1.printStackTrace();
                    }
                }                
            }
            
        };

        executorService.submit(runnable);
    }

}
