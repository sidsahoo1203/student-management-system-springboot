package com.cwm.studentmanagement.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * Automatically converts Render's PostgreSQL URL format (postgresql://...)
 * to the JDBC format required by Spring Boot (jdbc:postgresql://...).
 */
public class DatabaseUrlPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String datasourceUrl = environment.getProperty("SPRING_DATASOURCE_URL");

        if (datasourceUrl != null && !datasourceUrl.startsWith("jdbc:")) {
            Map<String, Object> props = new HashMap<>();
            props.put("spring.datasource.url", "jdbc:" + datasourceUrl);
            environment.getPropertySources().addFirst(new MapPropertySource("fixedDatasource", props));
        }
    }
}
