package com.G2T8.CS203WebApp.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.G2T8.CS203WebApp.model.Email;

@Service
public class EmailService {
    private JavaMailSender emailSender;
    private SpringTemplateEngine templateEngine;
    private final String from;

    @Autowired
    public EmailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine,
            @Value("${spring.mail.username}") String from) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.from = from;
    }

    /**
     * Send email using Thymeleaf templating engine
     * 
     * @param to            email to send to
     * @param subject       subject of message
     * @param templateModel variables for the template
     * @param filename      name of the HTML file to use as template
     * @throws MessagingException
     * @throws IOException
     */
    public void sendEmailWithTemplate(String to, String subject, String filename, Map<String, Object> templateModel)
            throws MessagingException, IOException {

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        String htmlBody = templateEngine.process(filename, thymeleafContext);

        sendHtmlMessage(to, subject, htmlBody);
    }

    /**
     * Sending a message using HTML
     * 
     * @param to       email to send to
     * @param subject  subject of the message
     * @param htmlBody HTML template
     * @throws MessagingException
     */
    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);
    }
}
