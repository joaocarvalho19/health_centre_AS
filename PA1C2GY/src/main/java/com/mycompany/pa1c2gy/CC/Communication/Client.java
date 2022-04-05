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

    private ObjectInputStream _in;
    
    private ObjectOutputStream _out;
    

    public Client(String hostName, int portNumb) {
        this.host = hostName;
        this.port = portNumb;
    }
    

    public boolean createSocket() {
        try {
            socket = new Socket(this.host, this.port);
            
            this._out = new ObjectOutputStream(this.socket.getOutputStream());
            this._in = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("success");
            return true;
        }
        catch(Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public void close() {
        try {
            this._in.close();
            this._out.close();
            this.socket.close();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }   
    }
    

    public Object readObject() {
        Object obj = null;
        try {
            obj = this._in.readObject();
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        
        return obj;
    }
    

    public void writeObject(Object obj) {
        try {
            this._out.writeObject(obj);
        }
        catch(Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
