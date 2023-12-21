package com.example.project;
import com.example.project.model.AppQuery;
import com.example.project.model.Utilisateur;
import com.mysql.cj.jdbc.Blob;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.security.cert.PolicyNode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddUtilisateurController {

    @FXML
    private VBox scrollContent;

    @FXML
    private ImageView search_button;

    @FXML
    private TextField search;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    @FXML
    private HBox user_Box;

    @FXML
    private ImageView pdp;

    @FXML
    private Label username;

    @FXML
    private ImageView add_button;

    @FXML
    private VBox vBox;
    private AppQuery appQuery = new AppQuery(); // Assuming AppQuery is your class with searchUtilisateur method

    @FXML
    private void initialize() {
        search_button.setOnMouseClicked(event -> searchUser());

    }

    private void searchUser() {
        String searchText = search.getText();
        List<Utilisateur> users = appQuery.searchUtilisateur(searchText);
        scrollContent.getChildren().clear();

        for (Utilisateur user : users) {
            HBox userBox = createUserBox(user);
            Pane bare = new Pane();
            bare.setPrefWidth(250);
            bare.setPrefHeight(2);
            bare.setStyle("-fx-background-color:  #46474A;");
            bare.setVisible(true);

            VBox container = new VBox();
            container.getChildren().addAll(userBox, bare);
            scrollContent.getChildren().add(container);
        }
    }

    private HBox createUserBox(Utilisateur user) {
        HBox userBox = new HBox();

        ImageView pdpImageView = createPdpImageView(user.getPdp()); // Assuming getAvatar returns the image path
        pdpImageView.setFitHeight(46);
        pdpImageView.setFitWidth(48);

        Label usernameLabel = new Label(user.getUsername());

        ImageView addButton = new ImageView(new Image("C:\\Java\\ChatApp\\project\\src\\main\\resources\\images\\person.jpeg")); // Set your own path for the add button image
        addButton.setFitHeight(41);
        addButton.setFitWidth(47);

        userBox.getChildren().addAll(pdpImageView, usernameLabel, addButton);

        // Set properties similar to the FXML-defined HBox
        userBox.setPrefHeight(66);
        userBox.setPrefWidth(360);

        pdpImageView.setTranslateX(15);
        pdpImageView.setTranslateY(9);

        usernameLabel.setPrefHeight(21);
        usernameLabel.setPrefWidth(148);
        usernameLabel.setTranslateX(28);
        usernameLabel.setTranslateY(9);

        addButton.setTranslateX(83);
        addButton.setTranslateY(13);

        addButton.setOnMouseClicked(event -> addFriendToTable(event, userBox)); // Set event on addButton

        return userBox;
    }
    private ImageView createPdpImageView(Blob blob) {
        try {
            byte[] imageData = blob.getBytes(1, (int) blob.length());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            Image image = new Image(inputStream);
            return new ImageView(image);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
            return new ImageView(); // Return a default ImageView in case of an error
        }
    }
    @FXML
    private void addFriendToTable(MouseEvent event, HBox userBox) {
        Label usernameLabel = (Label) userBox.getChildren().get(1); // Assuming username is the second child

        String friendUsername = usernameLabel.getText();
        String loggedInUsername = appQuery.Me.getUsername(); /* Get the username of the logged-in user */;

        // Insert the friendship into the Friendships table
        addFriendshipByUsername(loggedInUsername, friendUsername);
        removeUserBoxByUsername(friendUsername);
    }
    private void removeUserBoxByUsername(String username) {
        List<Node> nodesToRemove = new ArrayList<>();

        for (Node node : scrollContent.getChildren()) {
            if (node instanceof VBox) {
                VBox container = (VBox) node;
                HBox userBox = (HBox) container.getChildren().get(0); // Assuming the user box is the first child

                Label usernameLabel = (Label) userBox.getChildren().get(1); // Assuming username is the second child

                if (usernameLabel.getText().equals(username)) {
                    nodesToRemove.add(container);
                }
            }
        }

        scrollContent.getChildren().removeAll(nodesToRemove);
    }
    public void addFriendshipByUsername(String loggedInUsername, String friendUsername) {
        int loggedInUserId = appQuery.getUserIdByUsername(loggedInUsername);
        int friendUserId = appQuery.getUserIdByUsername(friendUsername);

        if (loggedInUserId != -1 && friendUserId != -1) {
            appQuery.addFriend(loggedInUserId, friendUserId);
            removeUserBoxByUsername(friendUsername);
        } else {
            // Handle case where user(s) are not found
        }
    }

}
