package com.shi.acsserver.repositories;

import com.shi.acsserver.entities.Amqpinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AmqpInfoRepository extends JpaRepository<Amqpinfo, Long>, QuerydslPredicateExecutor<Amqpinfo>{
    
}
