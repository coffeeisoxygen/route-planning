package com.coffeecode.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.coffeecode.view.map.ui.MapPanel;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    public MainFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("Route Planner");
        setSize(800, 600);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        MapPanel mapPanel = new MapPanel();
        add(mapPanel, BorderLayout.CENTER);  // Changed from EAST to CENTER
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
