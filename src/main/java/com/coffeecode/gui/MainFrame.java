package com.coffeecode.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import org.graphstream.graph.Graph;

import org.graphstream.graph.implementations.SingleGraph;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.models.LocationTableModel;

import com.coffeecode.gui.panels.LocationTablePanel;

import com.coffeecode.service.LocationService;

public class MainFrame extends JFrame {

    private final LocationController controller;
    private final LocationTableModel tableModel;
    private final Graph graph;

    public MainFrame(LocationService locationService) {
        this.tableModel = new LocationTableModel();
        this.controller = new LocationController(locationService, tableModel);
        this.graph = new SingleGraph("RouteGraph");

        initComponents();
        setupFrame();
    }

    private void initComponents() {
        LocationTablePanel tablePanel = new LocationTablePanel(tableModel, controller);
        //GraphPanel graphPanel = new GraphPanel(graph, tableModel);
        //StatisticsPanel statsPanel = new StatisticsPanel(tableModel);

        add(tablePanel, BorderLayout.WEST);
        //add(graphPanel, BorderLayout.CENTER);
        //add(statsPanel, BorderLayout.EAST);
    }

    private void setupFrame() {
        setTitle("Route Planner");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
