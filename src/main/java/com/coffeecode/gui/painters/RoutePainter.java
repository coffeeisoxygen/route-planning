package com.coffeecode.gui.painters;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

public class RoutePainter implements Painter<JXMapViewer> {

    private List<GeoPosition> track;

    public RoutePainter() {
        track = new ArrayList<>();
    }

    public void setTrack(List<GeoPosition> track) {
        this.track = track;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        g = (Graphics2D) g.create();

        // Convert geo to screen points
        List<Point> points = new ArrayList<>();
        for (GeoPosition gp : track) {
            points.add((Point) map.convertGeoPositionToPoint(gp));
        }

        // Set rendering
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(4));

        // Draw route
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        g.dispose();
    }
}
