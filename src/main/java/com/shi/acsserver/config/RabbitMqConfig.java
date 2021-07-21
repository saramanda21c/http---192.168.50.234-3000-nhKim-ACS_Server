package com.shi.acsserver.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shi.acsserver.entities.Amqpinfo;
import com.shi.acsserver.entities.QAmqpinfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Getter
@Setter
@Slf4j
public class RabbitMqConfig {    
    
    String host = "127.0.0.1";
    
    String username = "test";
    
    String password ="test"; 
    
    int port = 5672;   

    @Autowired
    QuerydslConfig querydslConfig;

    @Getter
    public List<Amqpinfo> AMQP_ENTITIES = new ArrayList<Amqpinfo>();

    public Collection<Declarable> queueCollection;
    public Collection<Declarable> exchangesCollection;
    
    @Bean
    public void getAmqpList(){
        JPAQueryFactory queryFactory = querydslConfig.jpaQueryFactory();
        QAmqpinfo qAmqpEntity = QAmqpinfo.amqpinfo;
        AMQP_ENTITIES = queryFactory.selectFrom(qAmqpEntity).fetch();        
        log.info("Load AMQPInfo From Database Complete");
    }

    @Bean
    Declarables queues(){                
        queueCollection = new ArrayList<Declarable>();
        AMQP_ENTITIES.forEach(q->queueCollection.add(new Queue(q.getName(), false, false, false)));        
        log.info("Create Queue Complete");
        return new Declarables(queueCollection);        
    }  
    
    
    @Bean
    Declarables exchanges(){                
        exchangesCollection = new ArrayList<Declarable>();
        AMQP_ENTITIES.forEach(q->exchangesCollection.add(new TopicExchange(q.getExchange(), false, false)));
        log.info("Create Exchange Complete");
        return new Declarables(exchangesCollection);
    }

        
    @Bean
    Declarables bindings(){                
        Collection<Declarable> bindingsCollection = new ArrayList<Declarable>();
        AMQP_ENTITIES.forEach(q->bindingsCollection.add(new Binding(q.getName(), DestinationType.QUEUE, q.getExchange(), q.getRoutkey(), null)));
        log.info("Create Binding Complete");
        return new Declarables(bindingsCollection);
    }

    @Bean    
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        
        return connectionFactory;
    }

    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
