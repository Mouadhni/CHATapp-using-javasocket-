package com.example.project.model;

import com.example.project.HelloController;
import javafx.application.Platform;

import java.io.*;
import java.net.*;

public class Client implements Runnable {
    private final String host;
    private static  int port;
    private Socket client;

    private final HelloController helloController; // Reference to HelloController instance

    public Client(String host, int port, HelloController helloController) {
        this.host = host;
        this.port = port;
        this.helloController = helloController; // Assign HelloController instance
    }


    @Override
    public void run() {
        try {
            client = new Socket(host, port);
            DataInputStream in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            String data2;
            while (true) {
                if (in.available() > 0) {
                    String data = in.readUTF();
                    System.out.println("Server(partie client): " + data);
                    Message msg=new Message(data);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }
    public void sendMessage(String message) {
        try {
            if (client != null) {
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                out.writeUTF(message+" "+AppQuery_main.Me.getId());
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {

        }

    }

    public  void closeClient() {
        try {
            if (client != null) {
                client.close();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public HelloController getHelloController() {
        return helloController;
    }
}
