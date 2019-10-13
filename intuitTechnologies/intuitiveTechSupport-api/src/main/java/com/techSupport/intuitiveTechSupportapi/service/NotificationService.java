package com.techSupport.intuitiveTechSupportapi.service;

import com.techSupport.intuitiveTechSupportapi.appConstants.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

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
