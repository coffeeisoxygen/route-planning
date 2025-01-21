package com.coffeecode.gui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.coffeecode.model.Locations;

public class LocationTableModel extends AbstractTableModel {

    private List<LocationTableListener> listeners = new ArrayList<>();
    private final List<Locations> locations = new ArrayList<>();
    private final String[] columns = {"Name", "Latitude", "Longitude", "Actions"};

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
                location.name();
            case 1 ->
                location.latitude();
            case 2 ->
                location.longitude();
            default ->
                null;
        };
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 0; // Only name is editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col == 0) {
            Locations loc = locations.get(row);
            locations.set(row, new Locations(loc.id(), (String) value,
                    loc.latitude(), loc.longitude()));
            fireTableCellUpdated(row, col);
            notifyListeners();
        }
    }

    public void addLocation(Locations location) {
        locations.add(location);
        fireTableRowsInserted(locations.size() - 1, locations.size() - 1);
        notifyListeners();
    }

    public void removeLocation(int row) {
        locations.remove(row);
        fireTableRowsDeleted(row, row);
        notifyListeners();
    }

    public void updateLocations(List<Locations> newLocations) {
        locations.clear();
        locations.addAll(newLocations);
        fireTableDataChanged();
    }

    public Locations getLocationAt(int row) {
        return locations.get(row);
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    // Observer pattern for GraphStream
    public interface LocationTableListener {

        void onLocationChanged(List<Locations> locations);
    }

    public void addListener(LocationTableListener listener) {
        listeners.add(listener);
    }

    public void setLocations(List<Locations> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
        fireTableDataChanged();
        notifyListeners();
    }

    private void notifyListeners() {
        for (LocationTableListener listener : listeners) {
            listener.onLocationChanged(new ArrayList<>(locations));
        }
    }
}
