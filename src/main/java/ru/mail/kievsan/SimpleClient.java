package ru.mail.kievsan;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.ConnectException;
import java.net.Socket;

import java.net.SocketException;
import java.util.Locale;

public class SimpleClient {

    private static Socket clientSocket;     // сокет для общения
    private static BufferedReader reader;   // ридер, читающий с консоли
    private static BufferedReader in;       // поток чтения из сокета
    private static PrintWriter out;         // поток записи в сокет


    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 8087;
        upClient(host, port);

    }

    static void upClient(String host, int port) throws IOException {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            reader = new BufferedReader(new InputStreamReader(System.in));
            String msg, resp;

            while (true) {
                System.out.println("Input your message: ");
                msg = reader.readLine().toUpperCase();  // ждём, пока клиент напишет в консоль
                out.println(msg);                       // "stop" - закрыть соединение

                resp = in.readLine().toUpperCase();
                System.out.println("\tServer:\t" + resp);

                if (msg.equals("STOP") || msg.equals("ЫЕЩЗ")) {
                    System.out.println("Server is closing...");
                    break;
                }
            }
        } finally {
            downClient(host, port);
        }
    }

    static void downClient(String host, int port) {
        try {
            if (!clientSocket.isClosed()) {
                // потоки закрыть:
                in.close();
                out.close();
                // сокет закрыть:
                clientSocket.close();
                System.out.println("Client socket for the Server on " + host + ":" + port + " closed");
            }
        } catch (NullPointerException | IOException ignored) {}
    }

}
