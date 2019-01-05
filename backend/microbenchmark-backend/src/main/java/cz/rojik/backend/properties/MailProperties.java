package cz.rojik.backend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String blindCopy;

    private String registrationText;

    private String registrationTextNonActive;

    private String activeText;

    public String getBlindCopy() {
        return blindCopy;
    }

    public void setBlindCopy(String blindCopy) {
        this.blindCopy = blindCopy;
    }

    public String getRegistrationText() {
        return registrationText;
    }

    public void setRegistrationText(String registrationText) {
        this.registrationText = registrationText;
    }

    public String getActiveText() {
        return activeText;
    }

    public void setActiveText(String activeText) {
        this.activeText = activeText;
    }

    public String getRegistrationTextNonActive() {
        return registrationTextNonActive;
    }

    public void setRegistrationTextNonActive(String registrationTextNonActive) {
        this.registrationTextNonActive = registrationTextNonActive;
    }
}
