package com.coffeecode.presentation.view;

import javax.swing.*;
import java.awt.*;
import org.springframework.stereotype.Component;
import com.coffeecode.presentation.view.components.MapComponent;

@Component
public class MainFrame extends JFrame {

    private final MapComponent mapComponent;

    public MainFrame(MapComponent mapComponent) {
        this.mapComponent = mapComponent;
        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("Route Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add map component
        add(mapComponent, BorderLayout.CENTER);

        // Set window size and position
        setSize(1000, 600);
        setLocationRelativeTo(null);
    }
}
