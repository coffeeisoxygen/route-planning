package com.coffeecode.config;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.coffeecode.gui.controllers",
    "com.coffeecode.gui.panels",
    "com.coffeecode.gui"
})
public class FrontEndConfig {

    @Bean
    public Graph routeGraph() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("RouteGraph");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        return graph;
    }

    @Bean
    public JXMapViewer mapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        return mapViewer;
    }

}
