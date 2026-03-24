package com.nodo.retotecnico;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RetoTecnicoApplication {

    public static void main(String[] args) {
        // Load .env file and set as environment variables
        Dotenv dotenv = Dotenv.configure()
            .directory(".")
            .ignoreIfMissing()
            .load();
        
        // Set environment variables AND system properties from .env file
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
            // Also try to make environment variables visible to Spring
        });
        
        SpringApplication.run(RetoTecnicoApplication.class, args);
    }

}
