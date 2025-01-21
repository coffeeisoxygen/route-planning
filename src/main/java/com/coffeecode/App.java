package com.coffeecode;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.coffeecode.config.AppConfig;
import com.coffeecode.config.GuiConfig;
import com.coffeecode.gui.MainFrame;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Create and configure Spring context
                context = new AnnotationConfigApplicationContext();
                context.register(AppConfig.class, GuiConfig.class);
                context.refresh();

                // Get MainFrame from context and display
                MainFrame mainFrame = context.getBean(MainFrame.class);
                mainFrame.setVisible(true);

            } catch (BeansException | IllegalStateException e) {
                logger.error("Application context initialization failed", e);
                if (context != null) {
                    context.close();
                }
                System.exit(1);
            }
        });

        // Add shutdown hook to properly close context
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (context != null && context.isActive()) {
                context.close();
            }
        }));
    }

    // Provide access to context for dialogs
    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
