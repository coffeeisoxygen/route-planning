package com.coffeecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.coffeecode.config.AppConfig;
import com.coffeecode.presentation.view.MainFrame;

import javax.swing.SwingUtilities;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            // Create Spring context with both configs
            AnnotationConfigApplicationContext context
                    = new AnnotationConfigApplicationContext(AppConfig.class);

            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = context.getBean(MainFrame.class);
                mainFrame.setVisible(true);
                logger.info("GUI initialized successfully");
            });

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
