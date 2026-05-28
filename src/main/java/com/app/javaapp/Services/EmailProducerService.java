package com.app.javaapp.Services;

import com.app.javaapp.Models.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.email.name}")
    private String emailExchange;

    @Value("${rabbitmq.binding.email.name}")
    private String emailRoutingKey;

    public void sendEmailToQueue(EmailMessage email) {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, email);
        System.out.println("Email job added to queue for: " + email.getTo());
    }
}