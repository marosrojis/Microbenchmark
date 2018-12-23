package cz.rojik.backend.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Endpoint configuration for services that use Client Settings service.
 */
@Configuration
@ConfigurationProperties(prefix = "path")
public class PathProperties {

    private String projects;

    private String results;

    private String linkProjects;

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getLinkProjects() {
        return linkProjects;
    }

    public void setLinkProjects(String linkProjects) {
        this.linkProjects = linkProjects;
    }
}
