package com.coffeecode.gui.models;

import javax.swing.table.AbstractTableModel;
import com.coffeecode.model.Locations;
import java.util.List;
import java.util.ArrayList;

public class LocationTableModel extends AbstractTableModel {

    private final List<Locations> locations = new ArrayList<>();
    private final String[] columns = {"ID", "Name", "Latitude", "Longitude"};

    @Override
    public int getRowCount() {
        return locations.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Locations location = locations.get(row);
        return switch (col) {
            case 0 ->
                location.id().toString().substring(0, 8);
            case 1 ->
                location.name();
            case 2 ->
                location.latitude();
            case 3 ->
                location.longitude();
            default ->
                null;
        };
    }

    public void updateLocations(List<Locations> newLocations) {
        locations.clear();
        locations.addAll(newLocations);
        fireTableDataChanged();
    }
}
