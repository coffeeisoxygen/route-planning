package com.coffeecode.config;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.swing_viewer.util.DefaultMouseManager;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.coffeecode.gui.MainFrame;
import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.handlers.GraphPanelHandler;
import com.coffeecode.gui.handlers.MapDialogHandler;
import com.coffeecode.gui.models.GraphPanelModel;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.gui.models.MapDialogModel;
import com.coffeecode.gui.panels.GraphPanel;
import com.coffeecode.gui.panels.MapDialog;
import com.coffeecode.service.DistanceService;
import com.coffeecode.service.LocationService;

@Configuration
@ComponentScan(basePackages = "com.coffeecode.gui")
public class GuiConfig {

    @Bean
    public MapDialogModel mapDialogModel() {
        return new MapDialogModel();
    }

    @Bean
    public LocationTableModel locationTableModel() {
        return new LocationTableModel();
    }

    @Bean
    public LocationController locationController(LocationService locationService,
            LocationTableModel tableModel) {
        return new LocationController(locationService, tableModel);
    }

    @Bean
    public MainFrame mainFrame(LocationController controller,
            LocationTableModel tableModel,
            ApplicationContext applicationContext) {
        return new MainFrame(controller, tableModel, applicationContext);
    }

    @Bean
    public JXMapViewer mapViewer(MapDialogModel model) {
        JXMapViewer viewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        viewer.setTileFactory(new DefaultTileFactory(info));
        viewer.setAddressLocation(model.getCurrentPosition());
        viewer.setZoom(model.getCurrentZoom());
        return viewer;
    }

    @Bean
    public MapDialogHandler mapDialogHandler(LocationController controller) {
        return new MapDialogHandler(controller);
    }

    @Bean
    public MapDialog mapDialog(MapDialogHandler handler, LocationController controller) {
        return new MapDialog(handler, controller);
    }

    @Bean
    public Graph locationGraph() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("Locations");
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.quality");
        return graph;
    }

    @Bean
    public SpringBox springLayout() {
        SpringBox layout = new SpringBox(false);
        layout.setForce(0.5);        // More stable force
        layout.setQuality(0.9);      // Higher quality
        layout.setStabilizationLimit(0.001);
        layout.setGravityFactor(0.9);
        return layout;
    }

    @Bean
    public GraphPanelHandler graphPanelHandler(GraphPanelModel model,
            DistanceService distanceService) {
        return new GraphPanelHandler(model, distanceService);
    }

    @Bean
    public GraphPanel graphPanel(Graph locationGraph,
            SpringBox springLayout,
            LocationTableModel tableModel,
            GraphPanelHandler handler,
            DefaultMouseManager mouseManager) {
        return new GraphPanel(locationGraph, springLayout, tableModel, handler, mouseManager);
    }

    @Bean
    public DefaultMouseManager mouseManager() {
        return new DefaultMouseManager();
    }
}
