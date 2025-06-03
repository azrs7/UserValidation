package org.example;

import java.sql.*;
import java.util.HashMap;

public class DatabaseHandler {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:ledger_system.db";

    public DatabaseHandler() {
        try {
            // Create a connection to the database
            connection = DriverManager.getConnection(DB_URL);

            // Create tables if they don't exist
            createTables();

            // Ensure the username column exists
            ensureUsernameColumnExists();

            System.out.println("Connected to SQLite database successfully.");
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "email TEXT PRIMARY KEY," +
                "password TEXT NOT NULL" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createUsersTable);
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ensureUsernameColumnExists() {
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(users);");
            boolean hasUsername = false;

            while (rs.next()) {
                if ("username".equalsIgnoreCase(rs.getString("name"))) {
                    hasUsername = true;
                    break;
                }
            }

            if (!hasUsername) {
                stmt.execute("ALTER TABLE users ADD COLUMN username TEXT;");
                System.out.println("Username column added to users table.");
            }
        } catch (SQLException e) {
            System.out.println("Error ensuring username column exists: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addUser(String username, String email, String password) {
        String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean userExists(String email) {
        String sql = "SELECT email FROM users WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if the email exists
        } catch (SQLException e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean validateUser(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Error validating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String getUsernameByEmail(String email) {
        String sql = "SELECT username FROM users WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching username: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public HashMap<String, String> getAllUsers() {
        HashMap<String, String> users = new HashMap<>();
        String sql = "SELECT email, password FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                users.put(email, password);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all users: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Patch users with null or empty usernames by assigning a default username.
     * Username format: "user" + rowid (SQLite internal row id)
     */
    public void patchNullUsernames() {
        String sql = "UPDATE users " +
                "SET username = 'user' || rowid " +
                "WHERE username IS NULL OR username = ''";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int updated = pstmt.executeUpdate();
            System.out.println("Updated " + updated + " users with generated usernames.");
        } catch (SQLException e) {
            System.out.println("Error patching usernames: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Optional: Create a unique index on username to prevent duplicates.
     */
    public void ensureUsernameUniqueIndex() {
        String sql = "CREATE UNIQUE INDEX IF NOT EXISTS idx_unique_username ON users(username);";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Ensured username uniqueness via index.");
        } catch (SQLException e) {
            System.out.println("Error creating unique index: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
