package cz.rojik.backend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class getting values from application.properties file. Properties which starts with prefix 'mail'.
 * @author Marek Rojik (marek@rojik.cz) on 05. 01. 2019
 */
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String blindCopy;

    public String getBlindCopy() {
        return blindCopy;
    }

    public void setBlindCopy(String blindCopy) {
        this.blindCopy = blindCopy;
    }

}
