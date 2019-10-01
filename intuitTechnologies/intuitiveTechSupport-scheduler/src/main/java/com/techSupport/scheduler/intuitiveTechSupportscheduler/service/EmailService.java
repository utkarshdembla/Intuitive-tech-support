package com.techSupport.scheduler.intuitiveTechSupportscheduler.service;

import com.techSupport.scheduler.intuitiveTechSupportscheduler.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Value(Constants.mailServiceHost)
    private String host;

    @Value(Constants.mailServicePort)
    private int port;

    @Value(Constants.mailServiceUsername)
    private String username;

    @Value(Constants.mailServicePassword)
    private String password;

    public void sendEmail(String setTo,String setSubject,String setBody)
    {
        JavaMailSenderImpl javaMailSender = setProperties();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(setTo);
        simpleMailMessage.setFrom("reply@IntuitiveSupport.com");
        simpleMailMessage.setSubject(setSubject);
        simpleMailMessage.setText(setBody);

        javaMailSender.send(simpleMailMessage);
    }

    private JavaMailSenderImpl setProperties()
    {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost("smtp.mailtrap.io");
        javaMailSenderImpl.setPort(2525);
        javaMailSenderImpl.setUsername("8577ade2de686d");
        javaMailSenderImpl.setPassword("7b3b94f9ea4946");

        return javaMailSenderImpl;
    }
}
