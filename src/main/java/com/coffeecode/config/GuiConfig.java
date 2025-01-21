package com.coffeecode.config;

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
import com.coffeecode.gui.handlers.MapDialogHandler;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.gui.models.MapDialogModel;
import com.coffeecode.gui.panels.MapDialog;
import com.coffeecode.service.LocationService;

@Configuration
@ComponentScan(basePackages = "com.coffeecode.gui")
public class GuiConfig {

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
    public JXMapViewer mapViewer() {
        JXMapViewer viewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        viewer.setTileFactory(new DefaultTileFactory(info));
        viewer.setAddressLocation(MapDialogModel.DEFAULT_LOCATIONS.get("Jakarta"));
        viewer.setZoom(7);
        return viewer;
    }

    @Bean
    public MapDialog mapDialog(MapDialogModel model, MapDialogHandler handler,
            LocationController controller) {
        return new MapDialog(model, handler, controller);
    }
}
