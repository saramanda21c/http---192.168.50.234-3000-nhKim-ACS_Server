package com.shi.acsserver.service.Message;

import org.springframework.amqp.core.Message;

public interface IMessageListener {
    
    void receiveMessage(final Message message);
}
