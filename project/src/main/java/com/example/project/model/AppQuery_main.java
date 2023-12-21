package com.example.project.model;

import com.example.project.AddUtilisateurController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppQuery_main {
    private static DatabaseConnector conn=new DatabaseConnector();
    public static Utilisateur Me=new Utilisateur(4,"Mouadhn","mouadhnika@gmail.com","mouad2003");
    public static List<Utilisateur> searchUtilisateur(String searchText) {
        List<Utilisateur> foundUsers = new ArrayList<>();

        try {
            Connection connection = conn.connect();
            String query = "SELECT * FROM Utilisateurs WHERE username LIKE ? AND username <> ? " +
                    "AND user_id  IN " +
                    "(SELECT F.friend_id FROM Friendships F WHERE F.user_id = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, Me.getUsername()); // Replace with appropriate username fetching
            preparedStatement.setInt(3, Me.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id=resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                Blob avatar = resultSet.getBlob("avatar");
                Date date = resultSet.getDate("date_inscription");

                // Create a Utilisateur object
                Utilisateur user = new Utilisateur(id,username, email, avatar);
                foundUsers.add(user);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundUsers;
    }
    public static List<Message> getMessagesBetweenUsers(int user1Id, int user2Id) {
        List<Message> messages = new ArrayList<>();
        System.out.println("id="+user1Id+"and" +user2Id);
        try { Connection connection = conn.connect();
            String query = "SELECT * FROM messages " +
                    "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                    "ORDER BY timestamp"; // Query to fetch messages between two users
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user1Id);
            preparedStatement.setInt(2, user2Id);
            preparedStatement.setInt(3, user2Id);
            preparedStatement.setInt(4, user1Id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Assuming Message class has necessary attributes and constructor
                Message message = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("sender_id"),
                        resultSet.getInt("receiver_id"),
                        resultSet.getString("message_content"),
                        resultSet.getTimestamp("timestamp")
                );
                messages.add(message);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }

        return messages;
    }
    public static boolean saveMessageToDatabase(int senderId, int receiverId, String messageText) {
        try {
            // Perform the database insert operation here using JDBC or your preferred ORM framework
            Connection connection = conn.connect();

            // Example using JDBC PreparedStatement with CURRENT_TIMESTAMP
            String insertQuery = "INSERT INTO messages (sender_id, receiver_id, message_content) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, senderId);
            preparedStatement.setInt(2, receiverId);
            preparedStatement.setString(3, messageText);

            int rowsAffected = preparedStatement.executeUpdate();

            // Check if the insertion was successful
            if (rowsAffected > 0) {
                // Inserted successfully
                return true;
            } else {
                // Failed to insert
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Return false if there was an error while inserting data
            return false;
        }
    }

}
