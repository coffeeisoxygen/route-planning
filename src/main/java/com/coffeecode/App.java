package com.coffeecode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.coffeecode.config.AppConfig;
import com.coffeecode.config.GuiConfig;
import com.coffeecode.gui.MainFrame;
import javax.swing.SwingUtilities;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (var context = new AnnotationConfigApplicationContext()) {
                context.register(AppConfig.class, GuiConfig.class);
                context.refresh();

                MainFrame mainFrame = context.getBean(MainFrame.class);
                mainFrame.setVisible(true);
            } catch (BeansException | IllegalStateException e) {
                logger.error("An error occurred while initializing the application context", e);
            }
        });
    }
}
