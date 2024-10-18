package com.nxh.notification.service;

import com.nxh.notification.dto.request.EmailRequest;
import com.nxh.notification.dto.request.SendEmailRequest;
import com.nxh.notification.dto.request.Sender;
import com.nxh.notification.dto.response.EmailResponse;
import com.nxh.notification.exception.AppException;
import com.nxh.notification.exception.ErrorCode;
import com.nxh.notification.repository.httpclient.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    EmailClient emailClient;

    @Value("${notification.email.brevo-apikey}")
    @NonFinal
    String apiKey = "";

    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("3N Badminton")
                        .email("nguyenxuanhiep2208@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
      throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
