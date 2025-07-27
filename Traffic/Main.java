package Traffic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Traffic Signal System");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Components
        JTextArea queueArea = new JTextArea();
        queueArea.setEditable(false);
        JLabel signalLabel = new JLabel("Signal: RED", SwingConstants.CENTER);

        JButton addVehicleBtn = new JButton("Add Vehicle");
        JButton emergencyBtn = new JButton("Add Emergency Vehicle");
        JButton signalBtn = new JButton("Change Signal");

        // Threaded Managers
        VehicleManager vehicleManager = new VehicleManager();
        TrafficLightController signalController = new TrafficLightController();

        Thread vehicleThread = new Thread(vehicleManager);
        Thread signalThread = new Thread(signalController);

        vehicleThread.start();
        signalThread.start();

        // Button Actions
        addVehicleBtn.addActionListener(e -> {
            Vehicle v = new Vehicle("Car", false);
            vehicleManager.addVehicle(v);
            queueArea.setText(String.join("\n", vehicleManager.getCurrentQueue()));
        });

        emergencyBtn.addActionListener(e -> {
            Vehicle v = new Vehicle("Ambulance", true);
            vehicleManager.addVehicle(v);
            queueArea.setText(String.join("\n", vehicleManager.getCurrentQueue()));
        });

        signalBtn.addActionListener(e -> {
            signalLabel.setText("Signal: " + signalController.getCurrentSignal());
        });

        // Layout
        JPanel topPanel = new JPanel();
        topPanel.add(signalLabel);

        JPanel btnPanel = new JPanel();
        btnPanel.add(addVehicleBtn);
        btnPanel.add(emergencyBtn);
        btnPanel.add(signalBtn);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(queueArea), BorderLayout.CENTER);
        frame.add(btnPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
