package gr.registry.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Εκκίνηση RESTful υπηρεσίας μητρώου πολιτών. */
@SpringBootApplication(scanBasePackages = "gr.registry")
public class RegistryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistryServiceApplication.class, args);
    }
}
