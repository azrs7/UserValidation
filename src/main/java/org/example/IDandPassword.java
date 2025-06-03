package org.example;

import java.util.HashMap;

public class IDandPassword {
    private HashMap<String, String> loginInfo = new HashMap<>(); // email -> password
    private DatabaseHandler dbHandler;

    public IDandPassword() {
        dbHandler = new DatabaseHandler();

        // Add default users if they don't exist
        addDefaultUsers();

        // Load all users from the database
        loginInfo = dbHandler.getAllUsers(); // Assuming it returns email-password pairs
    }

    private void addDefaultUsers() {
        // Format: {username, email, password}
        String[][] defaultUsers = {
                {"zahirara", "zahirara@gmail.com", "Chaecha"},
                {"najla", "najla@gmail.com", "Njla123"},
                {"budi", "budi@mail.com", "abc123"}
        };

        for (String[] user : defaultUsers) {
            if (!dbHandler.userExists(user[1])) {
                dbHandler.addUser(user[0], user[1], user[2]); // username, email, password
            }
        }
    }

    public HashMap<String, String> getLoginInfo() {
        return loginInfo;
    }

    public void addUser(String username, String email, String password) {
        dbHandler.addUser(username, email, password);
        loginInfo.put(email, password); // still use email as key for login
    }

    public boolean exists(String email) {
        return dbHandler.userExists(email);
    }

    public void closeConnection() {
        dbHandler.closeConnection();
    }
    public String getUsernameByEmail(String email) {
        return dbHandler.getUsernameByEmail(email);
    }

}
