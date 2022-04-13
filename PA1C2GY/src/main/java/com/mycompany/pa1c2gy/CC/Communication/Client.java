/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.CC.Communication;

import java.io.*;
import java.net.*;

/**
 *
 * @author joaoc
 */
public class Client {
    
    private Socket socket;

    private final String host;
    
    private final int port;
    

    public Client(String hostName, int portNumb) {
        this.host = hostName;
        this.port = portNumb;
        
    }
    

    public boolean createSocket() {
        try {
            this.socket = new Socket(this.host, this.port);
            
            
            System.out.println("success");
            this.socket.close();
            return true;
        }
        catch(Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public void close() {
        try {
            //this.in.close();
            //this.out.close();
            this.socket.close();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }   
    }
    

    /*public Object readObject() {
        Object obj = null;
        try {
            in = new ObjectInputStream(this.socket.getInputStream());
            obj = in.readObject();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        
        return obj;
    }*/
    

    public void writeObject(Object obj) {
        try {
            socket = new Socket(this.host, this.port);
            ObjectOutputStream out = new ObjectOutputStream(this.socket.getOutputStream());
            out.writeObject(obj);
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
