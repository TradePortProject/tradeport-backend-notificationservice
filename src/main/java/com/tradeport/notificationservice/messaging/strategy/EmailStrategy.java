package com.tradeport.notificationservice.messaging.strategy;

import com.tradeport.notificationservice.model.NotificationTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

@Component
public class EmailStrategy implements MessagingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EmailStrategy.class); // Use the correct class for the logger

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private String smtpPort;

    @Value("${mail.smtp.auth:true}") // Provide default value if not configured
    private boolean smtpAuth;

    @Value("${mail.smtp.starttls.enable:true}") // Provide default value if not configured
    private boolean starttlsEnable;

    @Value("${mail.recipient}")
    private String recipientEmail;

    @Override
    public void sendMessage(NotificationTO message) {
        logger.info("Sending email with message: {}", message);
        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(smtpAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(starttlsEnable));
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(username));
            emailMessage.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(message.getRecipientEmail())
            );
            emailMessage.setSubject(message.getSubject());
            emailMessage.setText(message.getMessage());

            Transport.send(emailMessage);
            logger.info("Email sent successfully!");
            message.setRecipientEmail(message.getRecipientEmail());
            message.setFromEmail(username);
            message.setSentTime(new Date());
            message.setEmailSend(true);
        } catch (MessagingException e) {
            message.setRecipientEmail(message.getRecipientEmail()); // Set recipient even if sending fails
            logger.error("Error sending email message: {}", e.getMessage(), e);
        }
    }
}