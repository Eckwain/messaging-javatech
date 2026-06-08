package com.example.messagingrabbitmq;

import org.springframework.stereotype.Component;

@Component
public class Receiver {

    public void receiveMessage(String message) {
        System.out.println("\n[Получено сообщение] -> " + message);
    }
}