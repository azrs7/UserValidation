package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.BasicStroke;


public class SignUpPage {
    JFrame frame = new JFrame();
    RoundedTextField usernameField = new RoundedTextField(20);
    RoundedTextField emailField = new RoundedTextField(20);
    RoundedPasswordField passwordField = new RoundedPasswordField(20);
    RoundedPasswordField confirmField = new RoundedPasswordField(20);
    JLabel messageLabel = new JLabel();

    public SignUpPage(IDandPassword idAndPassword) {
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmLabel = new JLabel("Confirm Password:");

        JButton registerButton = new JButton("Register") {
            private Shape shape;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fill with light pink
                g2.setColor(new Color(255, 182, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public boolean contains(int x, int y) {
                if (shape == null || !shape.getBounds().equals(getBounds())) {
                    shape = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
                }
                return shape.contains(x, y);
            }
        };

        registerButton.setFocusPainted(false);
        registerButton.setForeground(Color.BLACK); // Changed to black for better visibility
        registerButton.setBackground(new Color(255, 182, 210)); // light pink
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false); // This ensures the custom painting works correctly

        JButton loginButton = new JButton("Login") {
            private Shape shape;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(255, 182, 210)); // Light blue
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public boolean contains(int x, int y) {
                if (shape == null || !shape.getBounds().equals(getBounds())) {
                    shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
                }
                return shape.contains(x, y);
            }
        };
        loginButton.setFocusPainted(false);
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(new Color(255, 182, 210));
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);

        // Set bounds for the login button
        loginButton.setBounds(230, 290, 90, 40);     // placed right next to Register

        // Add functionality to open LoginPage
        loginButton.addActionListener(e -> {
            frame.dispose(); // Close the current sign-up frame
            new LoginPage(idAndPassword); // Open login page
        });

        usernameLabel.setForeground(Color.white );
        emailField.setBackground(Color.white);
        passwordField.setBackground(Color.white);
        confirmField.setBackground(Color.white);

        usernameLabel.setForeground(Color.black);
        emailLabel.setForeground(Color.black);
        passwordLabel.setForeground(Color.black);
        confirmLabel.setForeground(Color.black);
        messageLabel.setForeground(Color.black);

        usernameLabel.setBounds(20, 50, 120, 25);
        emailLabel.setBounds(20, 100, 120, 25);
        passwordLabel.setBounds(20, 150, 120, 25);
        confirmLabel.setBounds(20, 200, 120, 25);
        messageLabel.setBounds(100, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 20));

        usernameField.setBounds(150, 50, 200, 30);
        emailField.setBounds(150, 100, 200, 30);
        passwordField.setBounds(150, 150, 200, 30);
        confirmField.setBounds(150, 200, 200, 30);

        // Set the button bounds with a slightly larger size for better clickability
        // Register button position and action
        registerButton.setBounds(100, 290, 120, 40); // positioned on the left
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String confirm = String.valueOf(confirmField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Fields cannot be empty!");
            } else if (!password.equals(confirm)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Passwords do not match!");
            } else if (idAndPassword.exists(email)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Email already exists!");
            } else {
                idAndPassword.addUser(username, email, password);
                JOptionPane.showMessageDialog(frame, "Registration successful!");
                frame.dispose(); // close sign-up
                new LoginPage(idAndPassword); // return to login
            }
        });

        // Login button beside Register
        loginButton.setBounds(230, 290, 90, 40);
        loginButton.addActionListener(e -> {
            frame.dispose(); // Close the current sign-up frame
            new LoginPage(idAndPassword); // Open login page
        });

        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(null); // Important for absolute positioning
        frame.setContentPane(gradientPanel);

        // Add components to the frame
        // First add all labels and fields
        frame.add(usernameLabel);
        frame.add(emailLabel);
        frame.add(passwordLabel);
        frame.add(confirmLabel);
        frame.add(usernameField);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(confirmField);
        frame.add(messageLabel);
        frame.add(loginButton);


        // Add the register button last to ensure it's on top
        frame.add(registerButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    // You can reuse the RoundedTextField and RoundedPasswordField classes here too:
    static class RoundedTextField extends JTextField {
        private Shape shape;

        public RoundedTextField(int size) {
            super(size);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            g.setColor(new Color(255, 182, 210)); // orange border
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
        }

        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
            }
            return shape.contains(x, y);
        }
    }

    static class RoundedPasswordField extends JPasswordField {
        private Shape shape;

        public RoundedPasswordField(int size) {
            super(size);
            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            g.setColor(new Color(255, 182, 210)); // orange border
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
        }

        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30);
            }
            return shape.contains(x, y);
        }
    }

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            Color pink = new Color(255, 182, 210); // Light pink
            Color blue = new Color(173, 216, 230); // Light blue

            GradientPaint gp = new GradientPaint(0, 0, pink, 0, height, blue);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, width, height);
        }
    }

}

