package com.shi.acsserver.service.Message.Rabbit;

import com.shi.acsserver.service.Message.IMessageProducer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
//import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageProducer implements IMessageProducer{
    
    private static final String topicExchagne = "ACS-TO-MCS-EXCHANGE";
    //private static final String topicExchagne2 = "CCS-TO-MCS-EXCHANGE";

    private final RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;        
    }
        
    public String send(){
        String msg = "Hello";
        return sendMessage(msg);
    }


    @Override    
    public String sendMessage(Object message) {       

        try {
            rabbitTemplate.convertAndSend(topicExchagne, "ACS.MCS.IN", message.toString());        
            log.info("Send Message");                            
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return message.toString();

    }
}
