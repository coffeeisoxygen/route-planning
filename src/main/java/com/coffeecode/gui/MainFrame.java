package com.coffeecode.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Route Planner");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels and add them to the frame
        add(createWestPanel(), BorderLayout.WEST);
        add(createCenterAndEastPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JSplitPane createWestPanel() {
        // Table Locations
        String[] locationColumns = {"ID", "Name", "Longitude", "Latitude"};
        Object[][] locationData = {};
        JTable locationTable = new JTable(new DefaultTableModel(locationData, locationColumns));
        JScrollPane locationScrollPane = new JScrollPane(locationTable);

        // Table Edges
        String[] edgeColumns = {"From", "To", "Distance"};
        Object[][] edgeData = {};
        JTable edgeTable = new JTable(new DefaultTableModel(edgeData, edgeColumns));
        JScrollPane edgeScrollPane = new JScrollPane(edgeTable);

        // Split Pane for two tables
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, locationScrollPane, edgeScrollPane);
        splitPane.setDividerLocation(300); // Initial divider location
        splitPane.setResizeWeight(0.5); // Equal space for both tables initially
        splitPane.setPreferredSize(new Dimension(400, 0));

        return splitPane;
    }

    private JSplitPane createCenterAndEastPanel() {
        // Center Panel (Graph Visualization)
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Controls for visualization
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Input Posisi:"));
        controlPanel.add(new JComboBox<>(new String[]{"Posisi 1", "Posisi 2"}));
        controlPanel.add(new JLabel("Input Tujuan:"));
        controlPanel.add(new JComboBox<>(new String[]{"Tujuan 1", "Tujuan 2"}));
        controlPanel.add(new JLabel("Speed:"));
        controlPanel.add(new JSlider(1, 10, 5));
        JButton searchButton = new JButton("Search");
        controlPanel.add(searchButton);

        centerPanel.add(controlPanel, BorderLayout.NORTH);

        // Graph visualization placeholder
        JLabel graphVisualization = new JLabel("Graph Visualization (Placeholder)");
        graphVisualization.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(graphVisualization, BorderLayout.CENTER);

        // East Panel (Graph Statistics)
        JPanel eastPanel = new JPanel(new BorderLayout());
        JLabel statsLabel = new JLabel("<html><h3>Graph Statistics:</h3><br>"
                + "Nodes: 10<br>"
                + "Edges: 20<br>"
                + "Total Distance: 500 km</html>");
        statsLabel.setHorizontalAlignment(JLabel.CENTER);
        eastPanel.add(statsLabel, BorderLayout.CENTER);
        eastPanel.setPreferredSize(new Dimension(200, 0));

        // Split Pane for center and east panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPanel, eastPanel);
        splitPane.setDividerLocation(600); // Initial divider location
        splitPane.setResizeWeight(0.8); // Center panel takes more space initially

        return splitPane;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
