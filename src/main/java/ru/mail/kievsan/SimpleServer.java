package ru.mail.kievsan;


import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class SimpleServer {

    private static Socket clientSocket;
    private static ServerSocket serverSocket;


    public static void main(String[] args) throws IOException {
        int port = 8087;
        clearPort(port);
        upServerSocket(port);
    }


    public static void upServerSocket(int port) throws IOException {
        try {
            System.out.println("Server on " + port + " port is starting...");

            serverSocket  = new ServerSocket(port);
            clientSocket = serverSocket.accept();

            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
            {
                System.out.printf("New connection accepted on port %d%n", clientSocket.getPort());
                String msg;

                while (true) {
                    msg = in.readLine().toUpperCase();

                    if (msg.equals("STOP") || msg.equals("ЫЕЩЗ")) {
                        out.println(msg);
                        System.out.println("I got the client STOP command. Server is closing...");
                        break;
                    }
                    out.println("I got your message:\t'" + msg + "'.\tYour port: " + clientSocket.getPort());
//                    out.println(String.format("I got your message:\t--< %s >--\tYour port: %d%n", // будут косяки!!!
//                            msg, clientSocket.getPort()));
                    System.out.printf("I got the client message:\t%s\tClient port: %d%n",
                            msg, clientSocket.getPort());
                }
            } finally { downClient(port); }
        } finally { downServer(port); }
    }

    static void clearPort(int port) {
        downClient(port);
        downServer(port);
    }

    static void downServer(int port) {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();   }
            System.out.println("Server on " + port + " port closed");
        } catch (NullPointerException | IOException ignored) {}
    }

    static void downClient(int port) {
        try {
            if (!clientSocket.isClosed()) {
                // потоки закрыть:
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                // сокет закрыть:
                clientSocket.close();
                System.out.println("Client socket of the Server on " + port + " port closed");
            }
        } catch (NullPointerException | IOException ignored) {}
    }

}
