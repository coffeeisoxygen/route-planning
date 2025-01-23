package com.coffeecode.view.map;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class MapPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public MapPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Buat map viewer
        JXMapViewer mapViewer = createMapViewer();
        add(mapViewer, BorderLayout.CENTER);

        // Tambahkan ComboBox overlay
        JPanel overlayPanel = createOverlayPanel();
        mapViewer.setLayout(null); // Aktifkan absolute positioning untuk mapViewer
        mapViewer.add(overlayPanel);

        // Set posisi overlay
        overlayPanel.setBounds(10, 10, 150, 30); // X=10, Y=10, width=150, height=30
    }

    private JPanel createOverlayPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());

        // Use MapType enum for ComboBox
        JComboBox<MapType> mapTypeCombo = new JComboBox<>(MapType.values());
        mapTypeCombo.addActionListener(e -> {
            MapType selected = (MapType) mapTypeCombo.getSelectedItem();
            switchMapType(selected);
        });

        panel.add(mapTypeCombo, BorderLayout.CENTER);
        return panel;
    }

    private JXMapViewer createMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();

        // Set default OSM tile factory
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Set default zoom
        mapViewer.setZoom(8);

        // Center map on Indonesia (Jakarta coordinates)
        GeoPosition jakarta = new GeoPosition(-6.200000, 106.816666);
        mapViewer.setAddressLocation(jakarta);

        // Add interactions
        MouseInputListener mouseListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mouseListener);
        mapViewer.addMouseMotionListener(mouseListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));

        return mapViewer;
    }

    private void switchMapType(MapType mapType) {
        TileFactoryInfo info;
        switch (mapType) {
            case SATELLITE:
                info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);
                break;
            case VIRTUALEARTH:
                info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
                break;
            case HYBRID:
                info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);
                break;
            case OPENSTREETMAP:
            default:
                info = new OSMTileFactoryInfo();
                break;
        }

        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        JXMapViewer mapViewer = (JXMapViewer) getComponent(0);
        mapViewer.setTileFactory(tileFactory);
        mapViewer.repaint();
    }

}
