package com.example.project.model;

import com.example.project.HelloController;
import javafx.application.Platform;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server implements Runnable {
    private static int port;
    private ServerSocket server;
    private static List<ClientHandler> clients = new ArrayList<>();
    private String message_server;
    private final HelloController helloController; // Reference to HelloController instance


    public Server(int port,HelloController a) {
        this.port = port;
        this.helloController=a;

    }

    public static int getPort() {
        return port;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = server.accept();
                System.out.println("New client connected");

                boolean clientExists = false;

                // Check if the client already exists based on some criteria
                for (ClientHandler existingClient : clients) {
                    if (existingClient.getSocket().equals(socket)) {
                        clientExists = true;
                        break;
                    }
                }

                if (!clientExists) {
                    ClientHandler clientHandler = new ClientHandler(socket, this.helloController);
                    clients.add(clientHandler);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                } else {
                    System.out.println("Client already exists");
                    // Optionally, you might want to handle the case where the client already exists
                    // For instance, you could notify the client that it's already connected.
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeServer();
        }
    }

    public void closeServer() {
        try {
            if (server != null) {
                server.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private String message;
        private HelloController helloController;
        public ClientHandler(Socket socket,HelloController a) {
            this.socket = socket;
            this.helloController=a;

            try {
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (in.available() > 0) {
                        String data = in.readUTF();
                        System.out.println("run :"+data);
                        sendToOtherClients(data);
                    }}
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                closeClient();
            }
        }
        private void sendToOtherClients(String data) {
            // Iterate through 'clients' list and send 'data' to other clients
            for (ClientHandler client : clients) {
                String [] msg=data.split(" ");
                String[] slicedArray = Arrays.copyOfRange(msg, 0, msg.length - 1);

                StringBuilder concatenatedString = new StringBuilder();
                for (String element : slicedArray) {
                    concatenatedString.append(element).append(" ");
                }
                String result = concatenatedString.toString().trim();
                System.out.println( Client.getPort()+" == "+Integer.parseInt(msg[msg.length-1]));
                if (client.getPort()==this.getPort() && (Client.getPort()-1000)==Integer.parseInt(msg[msg.length-1])){
                    Message messi=new Message(result);
                    System.out.println("tiiiii");
                    Platform.runLater(() -> {
                        // Update the UI components here
                        helloController.displayReceivedMessage(messi);
                    });

                }
            }
        }

        public void closeClient() {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int getPort() {
            return  socket.getPort();
        }

        public Object getSocket() {
            return socket;
        }
    }
}
