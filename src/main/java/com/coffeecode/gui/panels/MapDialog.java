package com.coffeecode.gui.panels;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.exception.LocationOperationException;
import com.coffeecode.gui.handlers.MapDialogHandler;
import com.coffeecode.gui.models.MapDialogModel;

// ? [ ] MARK: Add MouseMotion
//  [ ] todo : FIx Implementation about waypoint
@Component
public class MapDialog extends JDialog {

    private final transient MapDialogHandler handler;
    private final JXMapViewer mapViewer;
    private GeoPosition selectedPosition;

    @Autowired
    public MapDialog(MapDialogHandler handler, LocationController controller) {
        this.handler = handler;
        this.mapViewer = handler.getMapViewer();

        setTitle("Select Location");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setModal(true);

        initComponents();
        setupListeners();
    }

    private void initComponents() {
        // Control panel at top
        JPanel controls = createControlPanel();

        // Main map in center
        setLayout(new BorderLayout());
        add(controls, BorderLayout.NORTH);
        add(mapViewer, BorderLayout.CENTER);
    }

    private JPanel createControlPanel() {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Map type selector with default selection
        JComboBox<String> mapType = new JComboBox<>(new String[]{"OpenStreetMap", "VirtualEarth"});
        mapType.setSelectedIndex(0); // Match default OSM
        mapType.addActionListener(e -> handler.switchMapType(mapType.getSelectedIndex()));

        // Location quick jump
        JComboBox<String> locations = new JComboBox<>(
                MapDialogModel.DEFAULT_LOCATIONS.keySet().toArray(String[]::new)
        );
        locations.addActionListener(e
                -> handler.goToLocation((String) locations.getSelectedItem())
        );

        // Zoom controls
        JButton zoomIn = new JButton("+");
        JButton zoomOut = new JButton("-");
        zoomIn.addActionListener(e -> handler.zoom(true));
        zoomOut.addActionListener(e -> handler.zoom(false));

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshMap());

        controls.add(new JLabel("Map:"));
        controls.add(mapType);
        controls.add(new JLabel("Go to:"));
        controls.add(locations);
        controls.add(new JLabel("Zoom:"));
        controls.add(zoomIn);
        controls.add(zoomOut);
        controls.add(refreshButton);

        return controls;
    }

    private void setupListeners() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private Point prev;

            @Override
            public void mouseClicked(MouseEvent e) {
                selectedPosition = mapViewer.convertPointToGeoPosition(e.getPoint());
                addNewLocation();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                prev = e.getPoint();
                mapViewer.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (prev != null) {
                    Point current = e.getPoint();
                    int dx = current.x - prev.x;
                    int dy = current.y - prev.y;

                    Rectangle view = mapViewer.getViewportBounds();
                    view.x -= dx;
                    view.y -= dy;

                    GeoPosition newCenter = mapViewer.convertPointToGeoPosition(
                            new Point(view.x + view.width / 2, view.y + view.height / 2)
                    );
                    mapViewer.setCenterPosition(newCenter);

                    prev = current;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                prev = null;
                mapViewer.setCursor(Cursor.getDefaultCursor());
            }
        };

        mapViewer.addMouseListener(mouseAdapter);
        mapViewer.addMouseMotionListener(mouseAdapter);

        // Keyboard shortcuts for zoom
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
        String name = JOptionPane.showInputDialog(this, "Enter location name:");
        if (name != null && !name.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Add location: " + name + " at "
                    + String.format("%.6f, %.6f",
                            selectedPosition.getLatitude(),
                            selectedPosition.getLongitude()),
                    "Confirm Add Location",
                    JOptionPane.OK_CANCEL_OPTION);

            if (confirm == JOptionPane.OK_OPTION) {
                try {
                    handler.saveLocation(name, selectedPosition);
                    // Don't close dialog - let user add more if needed
                } catch (LocationOperationException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error adding location: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void refreshMap() {
        // Store current view state
        GeoPosition currentPosition = mapViewer.getCenterPosition();
        int currentZoom = mapViewer.getZoom();

        // Force map to redraw tiles
        mapViewer.setAddressLocation(currentPosition);
        mapViewer.setZoom(currentZoom);

        // Invalidate and repaint
        mapViewer.invalidate();
        mapViewer.revalidate();
        mapViewer.repaint();
    }
}
