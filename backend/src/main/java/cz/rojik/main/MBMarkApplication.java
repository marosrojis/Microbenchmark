package cz.rojik.main;

import cz.rojik.constants.ConfigConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = ConfigConstants.COMPONENT_SCAN_BASE_PACKAGE)
@EnableAutoConfiguration
@EnableCaching
@EnableJpaRepositories
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
