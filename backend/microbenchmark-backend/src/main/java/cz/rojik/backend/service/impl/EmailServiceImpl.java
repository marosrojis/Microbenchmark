package cz.rojik.backend.service.impl;

import cz.rojik.backend.constants.PropertyConstants;
import cz.rojik.backend.dto.PropertyDTO;
import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.service.EmailService;
import cz.rojik.backend.service.PropertyService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static String FOLDER_WITH_EMAILS = "emails/";
    private static String REGISTRATION_TEMPLATE = FOLDER_WITH_EMAILS + "registration.txt";
    private static String REGISTRATION_NONACTIVE_TEMPLATE = FOLDER_WITH_EMAILS + "registrationNonActive.txt";
    private static String ACTIVATE_TEMPLATE = FOLDER_WITH_EMAILS + "activate.txt";

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PropertyService propertyService;

    @Override
    public void sendAfterRegistrationUser(String to, boolean isActive) {
        String text;
        try {
            if (isActive) {
                text = String.format(readEmailTemplateFromResource(REGISTRATION_TEMPLATE), to);
            }
            else {
                text = String.format(readEmailTemplateFromResource(REGISTRATION_NONACTIVE_TEMPLATE), to);
            }
        }  catch (IOException e) {
            LOGGER.warn("Email template was not found.", e);
            return;
        }
        sendSimpleMessage(to, "Registration to MBMark", text, true);
    }

    @Override
    public void sendAfterUpdateUser(String to) {
        String text;
        try {
            text = String.format(readEmailTemplateFromResource(ACTIVATE_TEMPLATE), to);
        } catch (IOException e) {
            LOGGER.warn("Email template {} was not found.", ACTIVATE_TEMPLATE, e);
            return;
        }
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
            try {
                PropertyDTO property = propertyService.getByKey(PropertyConstants.BLIND_COPY_EMAIL);
                message.setBcc(property.getValue());
            } catch (EntityNotFoundException e) {
                LOGGER.warn("Property {} was not found. Email will not be sent.", PropertyConstants.BLIND_COPY_EMAIL);
            }
        }

        emailSender.send(message);
    }

    /**
     * Read email template from maven folder 'resources'.
     * @param file file to read
     * @return file from folder 'resources'
     */
    private String readEmailTemplateFromResource(String file) throws IOException {
        LOGGER.trace("Read file {} from resources folder.", file);
        Resource resource = new ClassPathResource(file);
        String fileContent = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        return fileContent;
    }
}
