package com.coffeecode.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.springframework.context.ApplicationContext;

import com.coffeecode.gui.controllers.LocationController;
import com.coffeecode.gui.controllers.LocationOperationException;
import com.coffeecode.gui.models.LocationTableModel;
import com.coffeecode.model.Locations;

public class LocationTablePanel extends JPanel {

    private final LocationTableModel tableModel;
    private final ApplicationContext applicationContext;
    private final LocationController controller;

    public LocationTablePanel(LocationTableModel model, LocationController controller, ApplicationContext applicationContext) {
        this.tableModel = model;
        this.applicationContext = applicationContext;
        this.controller = controller;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 0));
        initComponents();
    }

    private void showAddDialog() {
        JTextField nameField = new JTextField(20);
        JTextField latField = new JTextField(10);
        JTextField lonField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Latitude:"));
        panel.add(latField);
        panel.add(new JLabel("Longitude:"));
        panel.add(lonField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Location", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double lat = Double.parseDouble(latField.getText());
                double lon = Double.parseDouble(lonField.getText());
                controller.addLocation(name, lat, lon);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Invalid coordinates format", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (LocationOperationException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected(JTable table) {
        int row = table.getSelectedRow();
        if (row != -1) {
            Locations location = tableModel.getLocationAt(row);
            try {
                controller.deleteLocation(location.id());
            } catch (LocationOperationException e) {
                JOptionPane.showMessageDialog(this,
                        e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initComponents() {
        // Table setup
        JTable locationTable = new JTable(tableModel);
        locationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        locationTable.getTableHeader().setReorderingAllowed(false);

        // Enable sorting
        locationTable.setAutoCreateRowSorter(true);

        // Context menu
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem updateItem = new JMenuItem("Edit Name");

        deleteItem.addActionListener(e -> deleteSelected(locationTable));
        updateItem.addActionListener(e -> editName(locationTable));

        contextMenu.add(deleteItem);
        contextMenu.add(updateItem);

        locationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            private void showContextMenu(MouseEvent e) {
                int row = locationTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    locationTable.setRowSelectionInterval(row, row);
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Column widths
        locationTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        locationTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        locationTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(locationTable);

        // CRUD Controls
        JPanel controls = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton mapButton = new JButton("Add with Map");

        addButton.addActionListener(e -> showAddDialog());
        deleteButton.addActionListener(e -> deleteSelected(locationTable));
        mapButton.addActionListener(e -> showMapDialog());

        controls.add(addButton);
        controls.add(deleteButton);
        controls.add(mapButton);

        add(scrollPane, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);
    }

    private void showMapDialog() {
        MapDialog dialog = applicationContext.getBean(MapDialog.class);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
    }

    private void editName(JTable table) {
        int row = table.getSelectedRow();
        if (row != -1) {
            table.editCellAt(row, 0);
        }
    }
}
