package org.example;

public class Main {
    public static void main(String[] args) {
        // Initialize the IDandPassword class which connects to the SQLite database
        IDandPassword idAndPassword = new IDandPassword();

        // Create and display the login page
        LoginPage loginPage = new LoginPage(idAndPassword);

        // Add a shutdown hook to close the database connection when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Closing database connection...");
            idAndPassword.closeConnection();
        }));

        DatabaseHandler dbHandler = new DatabaseHandler();

        // Patch usernames with null or empty values
        dbHandler.patchNullUsernames();

        // Optional: enforce unique usernames
        dbHandler.ensureUsernameUniqueIndex();

        // Your other logic here...

        dbHandler.closeConnection();

    }
}
