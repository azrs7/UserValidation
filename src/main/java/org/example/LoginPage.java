package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class LoginPage implements ActionListener {
    JFrame frame = new JFrame();
    JButton loginButton = createRoundedButton("Login");
    JButton resetButton = createRoundedButton("Reset");
    JButton signUpButton = createRoundedButton("Sign Up");

    // Use custom rounded fields
    RoundedTextField emailField = new RoundedTextField(20);
    RoundedPasswordField passwordField = new RoundedPasswordField(20);
    JLabel messageLabel = new JLabel();

    HashMap<String, String> loginInfo = new HashMap<>();
    private IDandPassword idAndPassword;
    private DatabaseHandler dbHandler;

    public LoginPage(IDandPassword idAndPassword) {
        this.idAndPassword = idAndPassword;
        this.dbHandler = new DatabaseHandler();
        loginInfo = idAndPassword.getLoginInfo();

        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");

        emailLabel.setBounds(50, 100, 75, 25);
        passwordLabel.setBounds(50, 150, 75, 25);
        emailLabel.setForeground(Color.BLACK);
        passwordLabel.setForeground(Color.BLACK);
        messageLabel.setBounds(100, 250, 250, 35);
        messageLabel.setFont(new Font(null, Font.ITALIC, 16));
        messageLabel.setForeground(Color.BLACK);

        emailField.setBounds(125, 100, 200, 30);
        passwordField.setBounds(125, 150, 200, 30);
        emailField.setBackground(Color.white);
        passwordField.setBackground(Color.white);

        loginButton.setBounds(30, 200, 100, 30);
        resetButton.setBounds(150, 200, 100, 30);
        signUpButton.setBounds(270, 200, 100, 30);

        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        signUpButton.addActionListener(e -> {
            frame.dispose();
            new SignUpPage(idAndPassword);
        });

        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(null); // Required for absolute positioning
        frame.setContentPane(gradientPanel);

        frame.add(emailLabel);
        frame.add(passwordLabel);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(resetButton);
        frame.add(signUpButton);
        frame.add(messageLabel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 350);
        frame.setLayout(null);
        frame.setVisible(true);

        // Style buttons
        styleButtons(loginButton, resetButton, signUpButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            emailField.setText("");
            passwordField.setText("");
        }

        if (e.getSource() == loginButton) {
            String email = emailField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (dbHandler.userExists(email)) {
                if (dbHandler.validateUser(email, password)) {
                    messageLabel.setForeground(Color.GREEN);
                    messageLabel.setText("Login successful!");

                    String username = dbHandler.getUsernameByEmail(email);
                    System.out.println("Username fetched: " + username); // Debug print
                    frame.dispose();
                    new WelcomePage(username); // Pass username to WelcomePage
                } else {
                    messageLabel.setForeground(Color.RED);
                    messageLabel.setText("Wrong password!");
                }
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Email not found!");
            }
        }
    }

    // Create rounded orange buttons (same as before)
    private JButton createRoundedButton(String text) {
        return new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 182, 210));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
    }

    private void styleButtons(JButton... buttons) {
        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setBorderPainted(false);
            btn.setForeground(Color.BLACK);
        }
    }

    // Rounded JTextField
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

    // Rounded JPasswordField
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

            GradientPaint gradient = new GradientPaint(0, 0, pink, 0, height, blue);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);
        }
    }

}
