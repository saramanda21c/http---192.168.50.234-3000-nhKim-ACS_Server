package com.shi.acsserver.controller;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.shi.acsserver.service.Message.Rabbit.MessageListener;
import com.shi.acsserver.service.Message.Rabbit.MessageProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MessageProducer producer;

    MessageListener listener;

    @GetMapping("/index/send")
    private String getSend(){
    
        producer.send();
        log.info("good");
        return "index";
    } 

    
}
