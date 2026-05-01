package com.cwm.studentmanagement.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Automatically converts Render/cloud PostgreSQL URL formats to valid JDBC URLs.
 * Handles: postgres://, postgresql://, and already-prefixed jdbc:postgresql:// URLs.
 */
public class DatabaseUrlPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String datasourceUrl = environment.getProperty("SPRING_DATASOURCE_URL");

        if (datasourceUrl == null || datasourceUrl.isBlank()) {
            return; // No cloud DB configured, will use H2 default
        }

        datasourceUrl = datasourceUrl.trim();
        String jdbcUrl;

        if (datasourceUrl.startsWith("jdbc:")) {
            // Already a valid JDBC URL
            jdbcUrl = datasourceUrl;
        } else if (datasourceUrl.startsWith("postgres://")) {
            // Render format: postgres://user:pass@host/db -> jdbc:postgresql://user:pass@host/db
            jdbcUrl = "jdbc:postgresql://" + datasourceUrl.substring("postgres://".length());
        } else if (datasourceUrl.startsWith("postgresql://")) {
            // Alternative format: postgresql://user:pass@host/db -> jdbc:postgresql://user:pass@host/db
            jdbcUrl = "jdbc:postgresql://" + datasourceUrl.substring("postgresql://".length());
        } else {
            // Unknown format, try using as-is with jdbc: prefix
            jdbcUrl = "jdbc:" + datasourceUrl;
        }

        Map<String, Object> props = new HashMap<>();
        props.put("spring.datasource.url", jdbcUrl);
        props.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
        environment.getPropertySources().addFirst(new MapPropertySource("fixedDatasource", props));

        System.out.println("=== Database URL configured: " + jdbcUrl.replaceAll(":[^@/]+@", ":****@") + " ===");
    }
}
