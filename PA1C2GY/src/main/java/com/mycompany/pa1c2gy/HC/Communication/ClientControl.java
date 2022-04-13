/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Communication;

import com.mycompany.pa1c2gy.HC.Entities.TControlCentre;

import com.mycompany.pa1c2gy.HC.Monitor.IEntranceHall_Patient;
import com.mycompany.pa1c2gy.HC.Entities.TPatient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 *
 * @author joaoc
 */
public class ClientControl extends Thread{
    
    
    private final Socket socket;
    private ObjectInputStream _in;
    private ObjectOutputStream _out;
    
    public ClientControl(Socket s){
        this.socket = s;
        
    }
    
    /**
    * Life cycle of the ClientThread Thread.
    */
    @Override
    public void run() {
        try {
            this._out = new ObjectOutputStream(this.socket.getOutputStream());
            this._in = new ObjectInputStream(this.socket.getInputStream());
            String obj;
            while ((obj = _in.readObject().toString()) != null){
                System.out.println("Client Control: "+obj);
                processMsg(obj);
            }
                
        } catch (IOException e) {
            System.err.println(e);
        }
        catch (ClassNotFoundException e) {
            System.err.println(e);
        }
    }
    
    public void processMsg(Object obj){
        String msg = obj.toString();
                    if(msg.contains("Start")){
                        String[] msg_list = msg.split("-");
                        int NoA = Integer.parseInt(msg_list[1]);
                        int NoC = Integer.parseInt(msg_list[2]);
                        int NoS = Integer.parseInt(msg_list[3]);
                        //startSimulation(NoA, NoC, NoS);
                    }
                    else{
                        System.out.println("NOP");
                    }
    }
}
