package com.example.project.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnector {

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/chat_app";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Method to establish a connection to the database
    public static Connection connect() {
        Connection connection = null;
        try {
            // Registering the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.out.println("Connection failed. Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
        }
        return connection;
    }

    // Method to close the connection
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    // Example usage
    public static void main(String[] args) {
        // Establishing a connection
        Connection conn = DatabaseConnector.connect();

        // Do database operations here using the 'conn' connection object

        // Closing the connection
        DatabaseConnector.closeConnection(conn);
    }
}
