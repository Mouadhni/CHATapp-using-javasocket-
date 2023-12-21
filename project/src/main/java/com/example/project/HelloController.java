package com.example.project;
import com.example.project.model.*;

import java.net.URL;
import java.sql.Timestamp;

import com.mysql.cj.jdbc.Blob;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController   {

    @FXML
    public  VBox chatVbox;
    @FXML
    private BorderPane borderPane;

    @FXML
    private HBox left_HBox;

    @FXML
    private VBox left_VBox;

    @FXML
    private ImageView enah_logo;

    @FXML
    private ImageView add_friend;

    @FXML
    private ImageView add_group;

    @FXML
    private ImageView go_back;

    @FXML
    private VBox right_VBox;
    @FXML
    private VBox friend_list;
    @FXML
    private HBox friends_box;
    @FXML
    private VBox searchicon_vbox;
    @FXML
    private HBox chat_username_box;
    @FXML
    private ImageView pdp_client1;

    @FXML
    private Label username_client;

    @FXML
    private Circle circle_enligne;

    @FXML
    private VBox right_VBox2;

    @FXML
    private HBox top_HBox;

    @FXML
    private ImageView client_pdp2;

    @FXML
    private Label username_client2;

    @FXML
    private ScrollPane chatScrollPane;



    @FXML
    private HBox client_box;

    @FXML
    private ImageView client_pdp3;

    @FXML
    private Label client_message;

    @FXML
    private Label date_message_client;

    @FXML
    private HBox server_box;

    @FXML
    private Label date_message_server;

    @FXML
    private Label client_message1;

    @FXML
    private ImageView pdp_server;

    @FXML
    private HBox bottom_HBox;

    @FXML
    private TextField message_text;
    @FXML
    private TextField search_friend;

    @FXML
    private Button sendbutton;
    @FXML
    private VBox add_utilisateur_vbox;

    private Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Utilisateur currentChatUser;
    private Client activeClient; // Maintain reference to the active client
    public Utilisateur getCurrentChatUser() {
        return currentChatUser;
    }

    public void setCurrentChatUser(Utilisateur currentChatUser) {
        this.currentChatUser = currentChatUser;
    }

    @FXML
    public void initialize() {
    }


    @FXML
    private void switchToAddUtilisateur() {
        try {
            // Store the original background color
            String originalStyle = add_utilisateur_vbox.getStyle();

            // Set the new background color
            add_utilisateur_vbox.setStyle("-fx-background-color: #d3d3d3;");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_Utilisateur.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new FXML
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 360, 443)); // Set width and height as needed
            stage.setTitle("Add Utilisateur");

            // Set a listener for when the stage is closed
            stage.setOnHidden(event -> {
                // Revert to the original background color when the stage is closed
                add_utilisateur_vbox.setStyle(originalStyle);
            });

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchfriend() {

        String style=searchicon_vbox.getStyle();
        searchicon_vbox.setStyle("-fx-background-color: #d3d3d3 ;");
        // Create a timeline for reverting the color after 1 second
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e -> {

            searchicon_vbox.setStyle("-fx-background-color: style;"); // Revert to the original color
        }));
        timeline.play();
        int serverPort = AppQuery.Me.getId()+1000; // Choose a suitable port number
        Server server = new Server(serverPort,this);
        Thread serverThread = new Thread(server);
        serverThread.start();
        String searchText = search_friend.getText();
        List<Utilisateur> users = AppQuery_main.searchUtilisateur(searchText);
        friend_list.getChildren().clear();

        for (Utilisateur user : users) {
            HBox userBox = createUserBox(user);

            Pane bare = new Pane();
            bare.setPrefWidth(250);
            bare.setPrefHeight(2);
            bare.setStyle("-fx-background-color:  #1d2951;");
            bare.setVisible(true);

            friend_list.getChildren().addAll(userBox, bare);
        }
    }

    private HBox createUserBox(Utilisateur user) {
        HBox userBox = new HBox();

        ImageView pdpImageView = createPdpImageView(user.getPdp()); // Assuming getAvatar returns the image path
        pdpImageView.setFitHeight(40);
        pdpImageView.setFitWidth(40);

        Label usernameLabel = new Label(user.getUsername());




        userBox.getChildren().addAll(pdpImageView, usernameLabel );

        // Set properties similar to the FXML-defined HBox
        userBox.setPrefHeight(40);
        userBox.setPrefWidth(263);
        userBox.setTranslateX(-12);
        pdpImageView.setTranslateX(15);
        pdpImageView.setTranslateY(1);

        usernameLabel.setPrefHeight(24);
        usernameLabel.setPrefWidth(148);
        usernameLabel.setTranslateX(28);
        usernameLabel.setTranslateY(9);



        userBox.setOnMouseClicked(event -> {
            // Establish connection when the user box is clicked
            connectToServer(AppQuery.Me.getId(), user.getId());
            chatBox(user);
            String originalStyle = userBox.getStyle();
            userBox.setStyle("-fx-background-color: #d3d3d3;"); // Change background color when clicked

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.7), e -> {
                        userBox.setStyle(originalStyle); // Revert to the original background color after 1 second
                    })
            );
            timeline.play();
        }); // Set event on addButton
        sendbutton.setOnMouseClicked(event -> sendMessage());
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
    private void chatBox(Utilisateur user) {
        currentChatUser = user;
        int yourUserId = AppQuery.Me.getId(); // Retrieve the ID of the logged-in user
        int otherUserId = user.getId(); //
        List<Message> messages = AppQuery_main.getMessagesBetweenUsers(yourUserId, otherUserId);

        // Clear the chatVbox to prepare for new messages
        chatVbox.getChildren().clear();
        // Set the username and profile picture in the chat_username_box
        ImageView pdpImageView = createPdpImageView(user.getPdp());
        pdpImageView.setFitHeight(53);
        pdpImageView.setFitWidth(34);

        chat_username_box.setStyle("-fx-background-color: ##d3d3d3 ;   -fx-border-radius: 10px;");

        // Apply styles to the profile picture
        pdpImageView.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #FFFFFF; -fx-border-radius: 5px; -fx-padding: 5px;");

        // Additional styling for the Username_client2 label
        Label usernameLabel = new Label(user.getUsername());
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #000000;");
        usernameLabel.setTranslateX(10);

        // Clear the chat_username_box before adding new content
        chat_username_box.setPrefHeight(70);
        chat_username_box.setPrefWidth(434);
        chat_username_box.getChildren().clear();
        chat_username_box.getChildren().addAll(pdpImageView, usernameLabel);
        for (Message message : messages) {
            if (message.getSenderId() == yourUserId) {
                // Message sent by the logged-in user
                displaySentMessage(message);
            } else {
                // Message received from the other user
                displayReceivedMessage(message);
            }


        }
//        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    private void connectToServer(int yourUserId, int otherUserId) {
        // Close the existing connection if it's not null

        if (activeClient != null ) {
            activeClient.closeClient();
        }
        Client client = new Client("localhost", otherUserId+1000,this); // Replace with the appropriate constructor parameters
        Thread clientThread = new Thread(client);
        clientThread.start();
        // Assign the new client as the active client
        activeClient = client;
    }

    public void displaySentMessage(Message message) {
        HBox sentMessage = createMessageContainer(message.getMessageContent(), message.getTimestamp(), true);
        chatVbox.getChildren().add(sentMessage);
        chatScrollPane.setVvalue(1.0);
    }

    public void displayReceivedMessage(Message message) {
        HBox receivedMessage = createMessageContainer(message.getMessageContent(), message.getTimestamp(), false);
        chatVbox.getChildren().add(receivedMessage);

        chatScrollPane.setVvalue(1.0);
    }


    private static HBox createMessageContainer(String content, Timestamp timestamp, boolean sentByCurrentUser) {
        HBox messageBox = new HBox();
        Label messageLabel = new Label(content);
        LocalDateTime localDateTime = timestamp.toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM");
        String formattedDateTime = localDateTime.format(formatter);
        Label timestampLabel = new Label(formattedDateTime) ; // Use the timestamp

        messageLabel.setWrapText(true);
        messageLabel.setPadding(new Insets(10)); // Add padding to the message label

        // Customize the appearance of the message box and labels based on sender
        if (sentByCurrentUser) {
            messageBox.setAlignment(Pos.CENTER_RIGHT);
            messageLabel.setTranslateX(-5);
            messageLabel.setStyle("-fx-background-color: #DCF8C6; -fx-background-radius: 10; -fx-padding: 8px;");
            timestampLabel.setStyle("-fx-font-size:8px; -fx-font-weight: bold; -fx-text-fill: #999999; -fx-padding: 2px 4px; -fx-background-color: #f2f2f2; -fx-background-radius: 4px;");
        } else {
            messageBox.setAlignment(Pos.CENTER_LEFT);
            messageLabel.setTranslateX(5);
            messageLabel.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-padding: 8px;");
            timestampLabel.setStyle("-fx-font-size: 8px; -fx-font-weight: bold; -fx-text-fill: #999999; -fx-padding: 2px 4px; -fx-background-color: #f2f2f2; -fx-background-radius: 4px;");
        }

        // Add message content and timestamp labels to the messageBox
        VBox messageContent = new VBox(messageLabel, timestampLabel);
        messageContent.setSpacing(5); // Adjust the spacing between message content and timestamp
        messageBox.getChildren().add(messageContent);

        return messageBox;
    }
    /////////////////////////////////////////////////////////
    @FXML
    private void sendMessage() {
        String messageText = message_text.getText(); // Get text from the messagetext TextField
        message_text.clear();

        // Get the IDs of the sender and receiver
        int yourUserId = AppQuery.Me.getId();
        int otherUserId = currentChatUser.getId();

        // Save the message to the database
        boolean messageSent = AppQuery_main.saveMessageToDatabase(yourUserId, otherUserId, messageText);

        if (currentChatUser!=null) {

            Message msg=new Message(messageText);
            displaySentMessage(msg);
            if (activeClient != null  ) {
                activeClient.sendMessage(messageText);
            }}
        // Create unique ports for each user pair or utilize a different strategy to differentiate communication

    }



}
