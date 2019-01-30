package cz.rojik.backend.service.impl;

import cz.rojik.backend.properties.MailProperties;
import cz.rojik.backend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MailProperties mailProperties;

    @Override
    public void sendAfterRegistrationUser(String to, boolean isActive) {
        String text;
        if (isActive) {
            text = String.format(mailProperties.getRegistrationText(), to);
        }
        else {
            text = String.format(mailProperties.getRegistrationTextNonActive(), to);
        }
        sendSimpleMessage(to, "Registration to MBMark", text, true);
    }

    @Override
    public void sendAfterUpdateUser(String to) {
        String text = String.format(mailProperties.getActiveText(), to);
        sendSimpleMessage(to, "Your account is active.", text, true);
    }

    /**
     * Create and send email to specific email
     * @param to user email
     * @param subject email subject
     * @param text text of email
     * @param sendBCC if email has to been sent to blind carbon copy user
     */
    private void sendSimpleMessage(String to, String subject, String text, boolean sendBCC) {
        LOGGER.trace("Send email to " + to + " with subject " + subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (sendBCC) {
            message.setBcc(mailProperties.getBlindCopy());
        }

        emailSender.send(message);
    }
}
