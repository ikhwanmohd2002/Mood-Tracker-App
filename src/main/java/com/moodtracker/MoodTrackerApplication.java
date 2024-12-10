package com.moodtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MoodTrackerApplication {
    public static void main(String[] args) {
        // Check if running in CLI mode
        boolean cliMode = args.length > 0 && args[0].equals("--cli");
        
        if (cliMode) {
            // Run as a console application
            new SpringApplicationBuilder(MoodTrackerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        } else {
            // Run as a web application
            SpringApplication.run(MoodTrackerApplication.class, args);
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }
}
