// package com.coffeecode.gui.panels;

// import javax.swing.*;

// import java.awt.*;

// import org.graphstream.graph.Graph;
// import org.graphstream.graph.Node;
// import org.graphstream.ui.view.Viewer;

// import java.util.List;

// import com.coffeecode.gui.models.LocationTableModel;

// public class GraphPanel extends JPanel implements LocationTableModel.LocationTableListener {

//     private final Graph graph;

//     public GraphPanel(Graph graph, LocationTableModel tableModel) {
//         this.graph = graph;
//         tableModel.addListener(this);

//         setLayout(new BorderLayout());
//         initComponents();
//     }

//     @Override
//     public void onLocationChanged(List<Locations> locations) {
//         updateGraph(locations);
//     }

//     private void updateGraph(List<Locations> locations) {
//         graph.clear();
//         locations.forEach(loc -> {
//             Node node = graph.addNode(loc.id().toString());
//             node.setAttribute("xy", loc.longitude(), loc.latitude());
//             node.setAttribute("label", loc.name());
//         });
//         updateGraphEdges();
//     }

//     private void initComponents() {
//         // Graph controls
//         JPanel controlPanel = createControlPanel();
//         add(controlPanel, BorderLayout.NORTH);

//         // Graph view
//         System.setProperty("org.graphstream.ui", "swing");
//         Viewer viewer = graph.display();
//         add((JPanel) viewer.getDefaultView(), BorderLayout.CENTER);
//     }

//     private JPanel createControlPanel() {
//         JPanel panel = new JPanel();
//         panel.add(new JLabel("Start:"));
//         panel.add(new JComboBox<>());
//         panel.add(new JLabel("End:"));
//         panel.add(new JComboBox<>());
//         return panel;
//     }
// }
