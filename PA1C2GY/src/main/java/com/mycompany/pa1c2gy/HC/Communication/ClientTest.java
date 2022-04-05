/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Communication;
import java.io.*;
import java.net.*;
import java.util.Scanner;
/**
 *
 * @author joaoc
 */
public class ClientTest {
    public static void main(String[] args) throws IOException {
 
        // establish a connection by providing host and port
        // number
        try (Socket socket = new Socket("localhost", 1234)) {
            
            // writing to server
            PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
  
            // reading from server
            BufferedReader in
                = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
  
            // object of scanner class
            Scanner sc = new Scanner(System.in);
            String line = null;
  
            while (!"exit".equalsIgnoreCase(line)) {
                
                // reading from user
                line = sc.nextLine();
  
                // sending the user input to server
                out.println(line);
                out.flush();
  
                // displaying server reply
                System.out.println("Server replied "
                                   + in.readLine());
            }
            
            // closing the scanner object
            sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}
