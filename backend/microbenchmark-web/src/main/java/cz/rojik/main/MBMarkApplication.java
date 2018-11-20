package cz.rojik.main;

import cz.rojik.constants.ConfigConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableCaching
@ComponentScan(basePackages = ConfigConstants.COMPONENT_SCAN_BASE_PACKAGE)
@EnableJpaRepositories(basePackages = ConfigConstants.REPOSITORIES_PACKAGE)
@EntityScan(basePackages = ConfigConstants.ENTITIES_PACKAGE)
@EnableTransactionManagement
public class MBMarkApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MBMarkApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MBMarkApplication.class, args);
    }
}

