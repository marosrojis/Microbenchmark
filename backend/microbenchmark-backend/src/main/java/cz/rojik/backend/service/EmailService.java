package cz.rojik.backend.service;

public interface EmailService {

    void sendAfterRegistrationUser(String to, boolean isActive);

    void sendAfterUpdateUser(String to);
}
