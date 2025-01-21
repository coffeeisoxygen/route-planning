package com.coffeecode.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.handlers.MapDialogHandler;
import com.coffeecode.gui.models.MapDialogModel;
import com.coffeecode.gui.models.MapDialogModel.TempLocation;

@Component
public class MapDialog extends JDialog {

    private final transient MapDialogModel model;
    private final transient MapDialogHandler handler;
    private final transient LocationController controller;
    private final JXMapViewer mapViewer;
    private GeoPosition selectedPosition;
    private final DefaultListModel<TempLocation> listModel;
    private final transient List<TempLocation> tempLocations;
    private final transient Set<Waypoint> waypoints;

    @Autowired
    public MapDialog(MapDialogModel model, MapDialogHandler handler, LocationController controller) {
        this.model = model;
        this.handler = handler;
        this.controller = controller;
        this.mapViewer = handler.getMapViewer();
        this.listModel = model.getListModel();
        this.tempLocations = model.getTempLocations();
        this.waypoints = model.getWaypoints();

        setTitle("Select Location");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setModal(true);

        initComponents();
        setupListeners();
    }

    private void initComponents() {
        // Create controls panel
        JPanel controls = createControlPanel();

        // Create location list panel
        JScrollPane listPane = createLocationListPanel();

        // Initialize map
        TileFactoryInfo info = new OSMTileFactoryInfo();
        mapViewer.setTileFactory(new DefaultTileFactory(info));
        mapViewer.setAddressLocation(MapDialogModel.DEFAULT_LOCATIONS.get("Jakarta"));
        mapViewer.setZoom(7);

        // Layout
        setLayout(new BorderLayout());
        add(controls, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);
        add(listPane, BorderLayout.EAST);
    }

    private JPanel createControlPanel() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Existing components
        JComboBox<String> mapType = new JComboBox<>(new String[]{"OpenStreetMap", "VirtualEarth"});
        mapType.addActionListener(e -> handler.switchMapType(mapType.getSelectedIndex()));

        JComboBox<String> locations = new JComboBox<>(MapDialogModel.DEFAULT_LOCATIONS.keySet().toArray(String[]::new));
        locations.addActionListener(e -> handler.goToLocation((String) locations.getSelectedItem()));

        // Add zoom controls
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        zoomIn.addActionListener(e -> handler.zoom(true));
        zoomOut.addActionListener(e -> handler.zoom(false));

        controls.add(new JLabel("Map:"));
        controls.add(mapType);
        controls.add(new JLabel("Go to:"));
        controls.add(locations);
        controls.add(new JLabel("Zoom:"));
        controls.add(zoomIn);
        controls.add(zoomOut);

        return controls;
    }

    private JScrollPane createLocationListPanel() {
        JList<TempLocation> locationList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(locationList);
        scrollPane.setPreferredSize(new Dimension(250, 0));

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save All");
        JButton clearButton = new JButton("Clear");

        saveButton.addActionListener(e -> saveLocations());
        clearButton.addActionListener(e -> clearLocations());

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.SOUTH);

        return scrollPane;
    }

    private void setupListeners() {
        // Existing mouse listener
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                addNewLocation();
            }
        });

        // Add keyboard listener for zoom
        mapViewer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_ADD) {
                    handler.zoom(true);
                } else if (e.getKeyCode() == KeyEvent.VK_MINUS || e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                    handler.zoom(false);
                }
            }
        });
        mapViewer.setFocusable(true);
    }

    private void addNewLocation() {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter location name:");
            if (name != null && !name.trim().isEmpty()) {
                handler.addLocation(name, selectedPosition);
                refreshMap(); // Refresh after adding
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding location: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveLocations() {
        handler.saveLocations();
        dispose();
    }

    private void clearLocations() {
        listModel.clear();
        tempLocations.clear();
        waypoints.clear();
        mapViewer.setOverlayPainter(null);
    }

    private void refreshMap() {
        // Store current view state
        GeoPosition currentPosition = mapViewer.getCenterPosition();
        int currentZoom = mapViewer.getZoom();

        // Clear and update waypoints
        mapViewer.setOverlayPainter(null);
        handler.updateWaypoints();

        // Force map to redraw tiles
        mapViewer.setAddressLocation(currentPosition);
        mapViewer.setZoom(currentZoom);

        // Invalidate the map viewer's layout and repaint
        mapViewer.invalidate();
        mapViewer.revalidate();
        mapViewer.repaint();
    }
}
