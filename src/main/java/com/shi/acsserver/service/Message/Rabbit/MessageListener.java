package com.shi.acsserver.service.message.rabbit;

import com.shi.acsserver.service.message.IMessageListener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageListener implements IMessageListener{
    
    @RabbitListener(queues = "MCS-TO-ACS")    
    public void receiveMessage(final Message message){
        //Reflection Method(); --process 처리

        //socket Send();  -- agv Send
        
        log.info(message.toString());        
    }

    // @RabbitListener(queues = "MCS-TO-CCS")
    // public void receiveMessageCCS(final Message message){
    //     log.info(message.toString());        
    // }  

    // @RabbitListener(queues= {"MCS-TO-ACS", "MCS-TO-CCS"})
    // public void receiveMessage(final Message message){
    //     log.info(message.toString());        
    // }
}
