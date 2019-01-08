package cz.rojik.backend.service;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
public interface EmailService {

    /**
     * Send email to user after successful registration. Email text is send based on active/nonactive parameter.
     * If user is active (enable) => send email about successful registration.
     * If user is nonactive => send email with information about confirm registration by admin.
     * @param to new user email
     * @param isActive status of user
     */
    void sendAfterRegistrationUser(String to, boolean isActive);

    /**
     * Send email to user with information about activation user's account.
     * @param to activate user email
     */
    void sendAfterUpdateUser(String to);
}
