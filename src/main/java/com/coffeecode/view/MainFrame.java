package com.coffeecode.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.coffeecode.view.map.context.MapViewContext;
import com.coffeecode.view.map.ui.MapPanel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

        // Use the singleton context
        MapViewContext context = MapViewContext.getInstance();
        MapPanel mapPanel = new MapPanel(context.getMapHandler());
        add(mapPanel, BorderLayout.CENTER);
    }

}
