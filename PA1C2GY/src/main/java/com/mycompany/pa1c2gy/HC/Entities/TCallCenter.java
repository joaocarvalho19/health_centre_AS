/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.IEntranceHall_CallCenter;
import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_CallCenter;
import com.mycompany.pa1c2gy.HC.Communication.Server;

import java.net.Socket;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingRoomHall_CallCenter;


/**
 *
 * @author joaoc
 */
public class TCallCenter extends Thread{
    
    private final IEntranceHall_CallCenter entranceHall;
    private final IWaitingRoomHall_CallCenter iwaitingHall;

    private boolean move;
    private String state;
    private ICallCenterHall_CallCenter iCallCenter;

    
    public TCallCenter(IEntranceHall_CallCenter sr1, IWaitingRoomHall_CallCenter iwaitingHall, ICallCenterHall_CallCenter cc) {
        this.entranceHall = sr1;
        this.iwaitingHall = iwaitingHall;

        this.iCallCenter = cc;

        this.move = false;
        this.state = "out";
    }
    

    @Override
    public void run() {
        while(true){
            String res = iCallCenter.process();
            switch(res){
                case "EvHall_1":
                case "EvHall_2":
                case "EvHall_3":
                case "EvHall_4":
                    entranceHall.allowPatient(res);
                    break;
                case "MedicalWait":
                    iwaitingHall.allowPatient(res);
            }
        }
    }

}
