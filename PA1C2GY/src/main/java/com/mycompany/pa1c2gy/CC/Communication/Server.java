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
public class Server  extends Thread{
    
    private ServerSocket listeningSocket;

    private Socket socket;

    private final int serverPort;

    private ObjectInputStream _in;

    private ObjectOutputStream _out;


    public Server(int portNumb) {
      this.serverPort= portNumb;
   }

    
    public void open() {
        try {
            System.out.print("Start CCP Server! "+this.serverPort);
            this.listeningSocket = new ServerSocket(this.serverPort);
        }
        catch(Exception e) {
            System.err.println(e);
        }
    }
    
        
    public void end() {
       try {
           this.listeningSocket.close();
       }
       catch(Exception e) {
           System.err.println(e);
       }
    }

    public Socket accept() throws SocketTimeoutException {
       Socket s = null;

       try {
           s = this.listeningSocket.accept();
           System.out.println("Accepted");
           
           this._in = new ObjectInputStream(s.getInputStream());
           this._out = new ObjectOutputStream(s.getOutputStream());
           
       }
       catch(Exception e) {
           System.err.println(e);
       }

       return s;
    }
    
    @Override
    public void run() {
        try { 
            while (true) {
	        new ClientHandler(this.listeningSocket.accept()).start();
	    }
	} catch (IOException e) {}
    }

    public void close() {
       try {
           this._in.close();
           this._out.close();
           this.socket.close();
       }
       catch(Exception e) {
           System.err.println(e);
       }
    }

    public Object readObject() {
       Object obj = null;

       try {
           obj = this._in.readObject();
       }
       catch(Exception e) {
           System.err.println(e);
       }

       return obj;
    }

    public void writeObject(Object obj) {
       try {
           this._out.writeObject(obj);
       }
       catch(Exception e) {
           System.err.println(e);
       }
    }
}