package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"controller", "service", "configuration", "exception", "seeder"})
@EntityScan("model")
@EnableJpaRepositories("repository")
@SpringBootApplication
public class UniSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniSysApplication.class, args);
    }
}
