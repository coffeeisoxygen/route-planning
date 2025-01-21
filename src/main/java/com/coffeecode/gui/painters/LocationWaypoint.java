package com.coffeecode.gui.painters;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import com.coffeecode.domain.model.Locations;

public class LocationWaypoint extends DefaultWaypoint {

    private final Locations location;
    private boolean selected;
    private boolean highlighted;

    public LocationWaypoint(Locations location) {
        super(new GeoPosition(location.latitude(), location.longitude()));
        this.location = location;
    }

    public Locations getLocation() {
        return location;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public String toString() {
        return location.name();
    }
}
