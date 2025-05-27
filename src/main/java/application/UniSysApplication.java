package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"controller", "service", "configuration", "exception", "seeder"})
@EntityScan("model")
@EnableJpaRepositories("repository")
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class UniSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniSysApplication.class, args);
    }
}
