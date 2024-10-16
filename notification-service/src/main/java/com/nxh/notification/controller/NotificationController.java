package com.nxh.notification.controller;

import com.nxh.event.dto.NotificationEvent;
import com.nxh.notification.dto.request.Recipient;
import com.nxh.notification.dto.request.SendEmailRequest;
import com.nxh.notification.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

  EmailService emailService;

  @KafkaListener(topics = "notification-delivery")
  public void listenNotificationDelivery(NotificationEvent message) {
    log.info("Message received: {}", message);
    emailService.sendEmail(
        SendEmailRequest.builder()
            .to(Recipient.builder().email(message.getRecipient()).build())
            .subject(message.getSubject())
            .htmlContent(message.getBody())
            .build());
  }
}
