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

    @Value(Constants.mailFrom)
    private String mailFrom;

    public void sendEmail(String setTo,String setSubject,String setBody)
    {
        JavaMailSenderImpl javaMailSender = setProperties();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(setTo);
        simpleMailMessage.setFrom(mailFrom);
        simpleMailMessage.setSubject(setSubject);
        simpleMailMessage.setText(setBody);

        javaMailSender.send(simpleMailMessage);
    }

    private JavaMailSenderImpl setProperties()
    {
        JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
        javaMailSenderImpl.setHost(host);
        javaMailSenderImpl.setPort(port);
        javaMailSenderImpl.setUsername(username);
        javaMailSenderImpl.setPassword(password);

        return javaMailSenderImpl;
    }
}
