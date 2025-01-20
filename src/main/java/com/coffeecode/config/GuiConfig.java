package com.coffeecode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.gui.MainFrame;
import com.coffeecode.service.LocationService;

@Configuration
public class GuiConfig {

    @Bean
    public LocationTableModel locationTableModel() {
        return new LocationTableModel();
    }

    @Bean
    public MainFrame mainFrame(LocationService locationService) {
        return new MainFrame(locationService);
    }
}
