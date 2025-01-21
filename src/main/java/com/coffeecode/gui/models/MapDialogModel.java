package com.coffeecode.gui.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.springframework.stereotype.Component;

@Component
public class MapDialogModel {

    private final DefaultListModel<TempLocation> listModel = new DefaultListModel<>();
    private final List<TempLocation> tempLocations = new ArrayList<>();
    private final Set<Waypoint> waypoints = new HashSet<>();
    private GeoPosition selectedPosition;

    public static final Map<String, GeoPosition> DEFAULT_LOCATIONS = Map.of(
            "Sukabumi", new GeoPosition(-6.91, 106.92),
            "Jakarta", new GeoPosition(-6.20, 106.81),
            "Bandung", new GeoPosition(-6.91, 107.60)
    );

    public static record TempLocation(String name, GeoPosition position, SaveStatus status) {

        @Override
        public String toString() {
            return String.format("%s (%.4f, %.4f) [%s]",
                    name, position.getLatitude(), position.getLongitude(), status);
        }
    }

    public enum SaveStatus {
        UNSAVED, SAVED, FAILED
    }

    // Getters
    public DefaultListModel<TempLocation> getListModel() {
        return listModel;
    }

    public List<TempLocation> getTempLocations() {
        return tempLocations;
    }

    public Set<Waypoint> getWaypoints() {
        return waypoints;
    }

    public GeoPosition getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(GeoPosition pos) {
        this.selectedPosition = pos;
    }
}
