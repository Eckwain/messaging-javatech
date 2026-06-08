package com.example.messagingrabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MessagingRabbitmqApplication {

    static final String fanoutExchangeName = "lab-fanout-exchange";

    @Bean
    FanoutExchange exchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    Queue autoDeleteQueue() {
        // Генерируем случайное уникальное имя для копии приложения, например: lab-queue-a1b2c3d4...
        String uniqueQueueName = "lab-queue-" + java.util.UUID.randomUUID().toString();

        // Параметры конструктора Queue:
        // name, durable (false), exclusive (true), autoDelete (true)
        return new Queue(uniqueQueueName, false, true, true);
    }

    @Bean
    Binding binding(Queue autoDeleteQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(autoDeleteQueue).to(exchange);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter, Queue autoDeleteQueue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(autoDeleteQueue.getName());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) {
        SpringApplication.run(MessagingRabbitmqApplication.class, args);
    }
}