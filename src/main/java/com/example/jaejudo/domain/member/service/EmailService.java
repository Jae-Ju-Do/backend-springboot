package com.example.jaejudo.domain.member.service;

import com.example.jaejudo.global.config.RedisConfig;
import com.example.jaejudo.global.exception.CannotSendMailException;
import com.example.jaejudo.global.exception.RedisNullException;
import com.example.jaejudo.global.exception.errorcode.CommonErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    // bean
    private final JavaMailSender mailSender;
    private final RedisConfig redisConfig;

    @Value("${spring.mail.username}")
    private String emailSender;

    // 이메일 발송
    public void sendEmail(String emailReceiver) {

        String key = generateKey();
        try {
            MimeMessage message = createMessage(emailReceiver, key);
            mailSender.send(message);
            log.info("Sent email to {}", emailReceiver);

            ValueOperations<String, Object> operations
                    = redisConfig.redisTemplate().opsForValue();
            operations.set(emailReceiver, key, 180, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CannotSendMailException(CommonErrorCode.CANNOT_SEND_EMAIL);
        }
    }

    // 인증코드 확인
    public boolean verifyEmail(String email, String key) {

        ValueOperations<String, Object> operations
                = redisConfig.redisTemplate().opsForValue();
        String redisKey = (String) operations.get(email);

        if (redisKey == null) {
            throw new RedisNullException(CommonErrorCode.REDIS_NULL);
        }

        if (redisKey.equals(key)) {
            log.info("Verify email successful");
            return true;
        }

        log.info("Verify email failed");
        return false;
    }

    // 이메일 생성
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

    // 인증코드 생성
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
