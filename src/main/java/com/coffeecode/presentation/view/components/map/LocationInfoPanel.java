package com.coffeecode.presentation.view.components.map;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LocationInfoPanel extends JPanel {

    private final LocationWaypoint waypoint;

    public LocationInfoPanel(LocationWaypoint waypoint) {
        this.waypoint = waypoint;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel(waypoint.getLocation().name()));
        add(new JLabel(String.format("%.6f, %.6f",
                waypoint.getLocation().latitude(),
                waypoint.getLocation().longitude())));
    }
}
