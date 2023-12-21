package com.example.project.model;
import com.example.project.AddUtilisateurController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppQuery {
    private DatabaseConnector conn=new DatabaseConnector();
    public static Utilisateur Me=new Utilisateur(4,"Mouadhn","mouadhnika@gmail.com","mouad2003");
    private void addUtilisateur(Utilisateur new_utilisateur){
        try {
            Connection conn= DatabaseConnector.connect();
            String query = "INSERT INTO Utilisateurs (username, email, password, avatar, date_inscription) VALUES (?, ?, ?, ?, ?)";

            // Creating a PreparedStatement
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            // Set values for the query parameters
            preparedStatement.setString(1, new_utilisateur.getUsername());
            preparedStatement.setString(2, new_utilisateur.getEmail());
            preparedStatement.setString(3, new_utilisateur.getPassword());
            preparedStatement.setBlob(4, new_utilisateur.getPdp()); // Assuming getAvatar() returns the path or byte array of the image
            preparedStatement.setDate(5, new_utilisateur.getDate());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // Method to search for users by username
    public List<Utilisateur> searchUtilisateur(String searchText) {
        List<Utilisateur> foundUsers = new ArrayList<>();

        try {
            Connection connection = conn.connect();
            String query = "SELECT * FROM Utilisateurs WHERE username LIKE ? AND username <> ? " +
                    "AND user_id NOT IN " +
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
    public void addFriend(int userId, int friendId) {
        try (Connection connection = conn.connect()) {
            String query = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, friendId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Friendship added successfully!");
            } else {
                System.out.println("Failed to add friendship.");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions appropriately
        }
    }

    public int getUserIdByUsername(String username) {
        int userId = -1; // Initialize with a default value indicating user not found

        try (Connection connection = conn.connect()) {
            String query = "SELECT user_id FROM Utilisateurs WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exceptions appropriately
        }

        return userId;
    }

}
