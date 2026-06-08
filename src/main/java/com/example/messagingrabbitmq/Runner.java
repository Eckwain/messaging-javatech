package com.example.messagingrabbitmq;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public Runner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Приложение запущено. Введите текст и нажмите Enter для отправки:");

        while (true) {
            String message = reader.readLine();

            if (message != null && !message.trim().isEmpty()) {
                rabbitTemplate.convertAndSend(
                        MessagingRabbitmqApplication.fanoutExchangeName,
                        "",
                        message
                );
            }
        }
    }
}