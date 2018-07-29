package ru.shishmakov.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class SimpleService {

    @PostConstruct
    public void init() {
        log.info("Hello world Spring Boot!");
    }
}
