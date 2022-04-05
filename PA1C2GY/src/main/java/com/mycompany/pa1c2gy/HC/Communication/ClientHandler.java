
package com.mycompany.pa1c2gy.HC.Communication;

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
            ObjectInputStream in = null;
    
            ObjectOutputStream out = null;
            
            try {
                    
                  // get the outputstream of client
                out = new ObjectOutputStream(clientSocket.getOutputStream());
  
                  // get the inputstream of client
                in = new ObjectInputStream(clientSocket.getInputStream());
  
                Object obj;
                while ((obj = in.readObject()) != null) {
  
                    // writing the received message from
                    // client
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