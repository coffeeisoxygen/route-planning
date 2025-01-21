package com.coffeecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        try {
            // Create and configure Spring context
            context = new AnnotationConfigApplicationContext();
            context.refresh();

            logger.info("Application context initialized successfully");

        } catch (BeansException | IllegalStateException e) {
            logger.error("Application context initialization failed", e);
            if (context != null) {
                context.close();
            }
            System.exit(1);
        }

        // Add shutdown hook to properly close context
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (context != null && context.isActive()) {
                context.close();
                logger.info("Application context closed");
            }
        }));
    }

    // Provide access to context for other components
    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
