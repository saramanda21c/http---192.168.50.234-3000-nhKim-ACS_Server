package com.shi.acsserver.service.message;

import org.springframework.amqp.core.Message;

public interface IMessageListener {
    
    void receiveMessage(final Message message);
}
