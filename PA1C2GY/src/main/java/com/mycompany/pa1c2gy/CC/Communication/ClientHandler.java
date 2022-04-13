
package com.mycompany.pa1c2gy.CC.Communication;

import java.net.*;
import java.io.*;

/**
 * Thread to read messages from a client connected to the OCC server.
 * @author Rafael Sá (104552), Luís Laranjeira (81526)
 */
public class ClientHandler extends Thread {
    private final Socket clientSocket;
  
        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
        
        @Override
        public void run()
        {
            
            try {
                    
                  // get the outputstream of client
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
  
                  // get the inputstream of client
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
  
                Object obj;
                while ((obj = in.readObject()) != null) {
  
                    // writing the received message from
                    // client
                    String msg = obj.toString();
                    if(msg.contains("ST")){
                        String[] msg_list = msg.split("-");
                        int NoA = Integer.parseInt(msg_list[1]);
                        int NoC = Integer.parseInt(msg_list[2]);
                        int NoS = Integer.parseInt(msg_list[3]);
                    }
                    else{
                        System.out.println("NOP");
                    }
                    System.out.printf(
                        " Sent from the client: %s\n",
                        obj);
                    out.writeObject(obj);
                }
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
  
                        clientSocket.close();
                    
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

}