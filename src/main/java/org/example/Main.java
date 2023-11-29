package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create a server socket that listens on port 8080
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started! \nListening for messages ...");

            while (true) {
                // Accept incoming client connections
                try (Socket client = serverSocket.accept()) {
                    // Log the incoming connection
                    System.out.println("Received incoming connection from: " + client.getInetAddress());

                    // Set up input streams to read client messages
                    InputStreamReader isr = new InputStreamReader(client.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder request = new StringBuilder();
                    String line;

                    // Read the client request line by line until it's blank
                    while (!(line = br.readLine()).isBlank()) {
                        request.append(line).append("\r\n");
                    }

                    // Parse the requested resource from the HTTP request
                    String resource = request.toString().split("\n")[0].split(" ")[1];
                    System.out.println(resource);

                    // Set up an output stream to send response back to the client
                    OutputStream clientOutput = client.getOutputStream();

                    // Send the HTTP response header
                    clientOutput.write(("HTTP/1.1 200 OK\r\n\r\n").getBytes());

                    // Check the requested resource and send appropriate responses
                    if (Objects.equals(resource, "/yoti")) {
                        // If the resource is '/yoti', send an image
                        FileInputStream image = new FileInputStream("images/p-80-java.webp");
                        clientOutput.write(image.readAllBytes());
                    } else if (Objects.equals(resource, "/hello")) {
                        // If the resource is '/hello', send 'Hello World!'
                        clientOutput.write("Hello World!".getBytes());
                    } else {
                        // For any other resource, send a default message
                        clientOutput.write("What the hell are you looking for?".getBytes());
                    }

                    // Flush and close the output stream
                    clientOutput.flush();
                }
            }
        }
    }
}