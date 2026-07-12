package com.whatsapp.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    // List to keep track of all connected clients
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void main(String[] args) throws Exception {
        System.out.println("WhatsApp Server is running...");
        ServerSocket listener = new ServerSocket(7777);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public Handler(Socket socket) { this.socket = socket; }

       public void run() {
    try {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        
        synchronized (clientWriters) { clientWriters.add(out); }

        // --- DECLARE THE VARIABLE HERE ---
        String input; 
        
        // Now the loop will work
        while ((input = in.readLine()) != null) {
            System.out.println("SERVER LOG (Encrypted): " + input); 
            
            for (PrintWriter writer : clientWriters) {
                writer.println(input);
            }
        }
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    } finally {
        if (out != null) { clientWriters.remove(out); }
        try { socket.close(); } catch (IOException e) {}
    }
}
        }
    }
