/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Communication;
import java.io.*;
import java.net.*;
/**
 *
 * @author joaoc
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {

        ServerSocket server = null;
  
        try {
  
            // server is listening on port 1234
            server = new ServerSocket(1234);
            server.setReuseAddress(true);
  
            // running infinite loop for getting
            // client request
            while (true) {
  
                // socket object to receive incoming client
                // requests
                Socket client = server.accept();
  
                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                                   + client.getInetAddress()
                                         .getHostAddress());
  
                // create a new thread object
                ClientHandler clientSock
                    = new ClientHandler(client);
  
                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
  
    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
  
        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
  
        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                    
                  // get the outputstream of client
                out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
  
                  // get the inputstream of client
                in = new BufferedReader(
                    new InputStreamReader(
                        clientSocket.getInputStream()));
  
                String line;
                while ((line = in.readLine()) != null) {
  
                    // writing the received message from
                    // client
                    System.out.printf(
                        " Sent from the client: %s\n",
                        line);
                    out.println(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
