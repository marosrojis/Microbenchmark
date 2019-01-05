package cz.rojik.backend.service;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface EmailService {

    void sendAfterRegistrationUser(String to, boolean isActive);

    void sendAfterUpdateUser(String to);
}
