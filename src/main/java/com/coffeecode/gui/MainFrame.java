package com.coffeecode.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.gui.panels.GraphPanel;
import com.coffeecode.gui.panels.LocationTablePanel;

@Component
public class MainFrame extends JFrame {

    private final LocationController controller;
    private final LocationTableModel tableModel;
    private final ApplicationContext applicationContext;

    @Autowired
    public MainFrame(LocationController controller,
            LocationTableModel tableModel,
            ApplicationContext applicationContext) {
        this.controller = controller;
        this.tableModel = tableModel;
        this.applicationContext = applicationContext;
        initComponents();
        setupFrame();
    }

    private void initComponents() {
        LocationTablePanel tablePanel = new LocationTablePanel(tableModel, controller, applicationContext);
        GraphPanel graphPanel = applicationContext.getBean(GraphPanel.class);

        add(tablePanel, BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);
    }

    private void setupFrame() {
        setTitle("Route Planner");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
