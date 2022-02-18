package com.einvoive.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toAddress, String subject, String emailBody){
       try {
           SimpleMailMessage msg = new SimpleMailMessage();
           msg.setTo(toAddress);
           msg.setSubject(subject);
           msg.setText(emailBody);
           mailSender.send(msg);
           System.out.println("Email Sent to: " + toAddress);
         }catch(Exception ex){
           System.out.println("Error " +ex.getMessage());
       }
    }
}
