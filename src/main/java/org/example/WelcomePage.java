package org.example;

import javax.swing.*;
import java.awt.*;

public class WelcomePage {
    private JFrame frame;
    private JLabel welcomeLabel;

    public WelcomePage(String username) {
        // Frame setup
        frame = new JFrame("Welcome");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Gradient panel for background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color pink = new Color(255, 182, 210);
                Color blue = new Color(173, 216, 230);
                GradientPaint gp = new GradientPaint(0, 0, pink, 0, height, blue);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        gradientPanel.setLayout(new BoxLayout(gradientPanel, BoxLayout.Y_AXIS));
        gradientPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.BLACK);


        // Add components to panel
        gradientPanel.add(Box.createVerticalGlue());
        gradientPanel.add(welcomeLabel);
        gradientPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        gradientPanel.add(Box.createVerticalGlue());

        frame.add(gradientPanel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null); // Center window
        frame.setVisible(true);
    }
}
