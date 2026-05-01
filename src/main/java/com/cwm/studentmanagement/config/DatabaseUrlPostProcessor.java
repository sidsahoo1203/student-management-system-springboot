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
        Map<String, Object> props = new HashMap<>();

        try {
            // If the URL has credentials like postgres://user:pass@host/db
            // The JDBC driver will reject it if left in the URL string, so we must parse it out.
            String cleanUriStr = datasourceUrl;
            if (cleanUriStr.startsWith("jdbc:")) {
                cleanUriStr = cleanUriStr.substring(5); // Remove jdbc: prefix to parse with java.net.URI
            }
            
            java.net.URI uri = new java.net.URI(cleanUriStr);
            String host = uri.getHost();
            int port = uri.getPort() > 0 ? uri.getPort() : 5432;
            String path = uri.getPath(); // includes the leading slash and DB name
            String userInfo = uri.getUserInfo();
            
            String finalJdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
            
            // Add SSL mode query parameter which is required for Render databases
            String query = uri.getQuery();
            if (query != null && !query.isEmpty()) {
                finalJdbcUrl += "?" + query;
            }

            props.put("spring.datasource.url", finalJdbcUrl);

            // Extract username and password from the URI and set them as separate properties
            if (userInfo != null) {
                String[] parts = userInfo.split(":", 2);
                props.put("spring.datasource.username", parts[0]);
                if (parts.length > 1) {
                    props.put("spring.datasource.password", parts[1]);
                }
            }
            
            System.out.println("=== Database URL configured: jdbc:postgresql://" + host + ":" + port + path + " ===");

        } catch (Exception e) {
            // Fallback if parsing fails - just prepend jdbc: and hope for the best
            String fallbackUrl = datasourceUrl.startsWith("jdbc:") ? datasourceUrl : "jdbc:" + datasourceUrl;
            props.put("spring.datasource.url", fallbackUrl);
            System.out.println("=== Database URL fallback: " + fallbackUrl.replaceAll(":[^@/]+@", ":****@") + " ===");
        }

        props.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
        props.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
        environment.getPropertySources().addFirst(new MapPropertySource("fixedDatasource", props));
    }
}
