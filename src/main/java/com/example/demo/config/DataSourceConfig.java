package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Configuration
@DependsOn("dotEnvConfig") // DotEnvConfig must be initialized before running this class since the db
                           // relies on dotenv config.
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        System.console().printf("hay %s ", System.getProperty("DB_USERNAME"));
        System.console().printf("hay %s ", System.getProperty("JWT_SECRET"));
        return DataSourceBuilder.create()
                .url(url)
                .username(System.getProperty("DB_USERNAME"))
                .password(System.getProperty("DB_PASSWORD"))
                .driverClassName(driverClassName)
                .build();
    }
}