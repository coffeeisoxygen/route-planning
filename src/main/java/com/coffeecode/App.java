package com.coffeecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.coffeecode.config.AppConfig;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            // Create Spring context with both configs
            AnnotationConfigApplicationContext context
                    = new AnnotationConfigApplicationContext(AppConfig.class);

            logger.info("Application started successfully");

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                context.close();
                logger.info("Application shutdown complete");
            }));

        } catch (Exception e) {
            logger.error("Application failed to start", e);
            System.exit(1);
        }
    }
}
