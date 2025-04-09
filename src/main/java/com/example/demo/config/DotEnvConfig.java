package com.example.demo.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DotEnvConfig {

    @Autowired
    private Environment environment;

    @PostConstruct
    public void loadEnv() {
        String activeProfile = environment.getProperty("spring.profiles.active", "dev");
        String envFile = activeProfile + ".env";

        Dotenv dotenv = Dotenv.configure()
                .filename(envFile)
                .load();

        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
    }
}