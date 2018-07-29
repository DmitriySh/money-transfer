package ru.shishmakov.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SimpleService {

    @PostConstruct
    public void init() {
        System.out.println("Hello world Spring Boot!");
    }
}
