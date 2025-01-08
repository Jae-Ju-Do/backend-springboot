package com.example.jaejudo.domain.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final String emailSender = System.getenv("SMTP_ADDR");
    private String key;

    public void sendEmail(String emailReceiver) {

        key = generateKey();
        try {
            MimeMessage message = createMessage(emailReceiver, key);
            mailSender.send(message);
            log.info("Sent email to {}", emailReceiver);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public boolean verifyEmail(String key) {

        // todo: 동시에 여러명 로그인하면 문제생길듯? 리팩토링 필요
        if (key.equals(this.key)) {
            log.info("Verify email successful");
            this.key = null;
            return true;
        }
        log.info("Verify email failed");
        return false;
    }

    private MimeMessage createMessage(String emailReceiver, String key) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(emailSender);
        message.setRecipients(MimeMessage.RecipientType.TO, emailReceiver);
        message.setSubject("이메일 인증코드 발송");

        String body = "";
        body += "<h3>요청하신 인증 번호입니다.</h3>";
        body += "<h1>" + key + "</h1>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    private String generateKey() {

        SecureRandom random = new SecureRandom();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0 -> key.append((char) ('a' + random.nextInt(26)));
                case 1 -> key.append((char) ('A' + random.nextInt(26)));
                case 2 -> key.append(random.nextInt(10));
            }
        }
        return key.toString();
    }
}
