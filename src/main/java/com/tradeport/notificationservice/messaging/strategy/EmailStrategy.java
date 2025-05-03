package com.tradeport.notificationservice.messaging.strategy;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import com.amazonaws.services.simpleemail.model.Message;
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
    private static final Logger logger = LoggerFactory.getLogger(MessagingStrategy.class); // Use the correct class for the logger
    @Value("${aws.access.key}")
    private String aws_access_key;

    @Value("${aws.secret.key}")
    private String aws_secret_key;

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

    //@Value("${mail.recipient}")
    //private String recipientEmail;

    @Value("${mail.fromemail}")
    private String fromEmail;


     @Override
    public void sendMessage(NotificationTO message) {

        try {
            // Initialize AWS Credentials
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(aws_access_key, aws_secret_key);

            // Create SES Client
            AmazonSimpleEmailService sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_SOUTHEAST_1) // Change to your AWS region
                    .build();

            // Create Email Request
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(fromEmail)
                    .withDestination(new Destination().withToAddresses(message.getRecipientEmail()))
                    .withMessage(new Message()
                            .withSubject(new Content().withCharset("UTF-8").withData(message.getSubject()))
                            .withBody(new Body().withText(new Content().withCharset("UTF-8").withData(message.getMessage()))));

            // Send Email
            sesClient.sendEmail(request);
            message.setSentTime(new Date());
            message.setFromEmail(fromEmail);
            message.setEmailSend(true);
            logger.info("Email sent successfully!");

        } catch (Exception e) {
            message.setRecipientEmail(message.getRecipientEmail()); // Set recipient even if sending fails
            logger.error("Error sending email message: {}", e.getMessage(), e);
        }
    }


}
