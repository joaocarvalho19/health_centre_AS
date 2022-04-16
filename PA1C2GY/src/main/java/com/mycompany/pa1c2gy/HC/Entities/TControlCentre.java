/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.IControlCentre;

import com.mycompany.pa1c2gy.HC.Communication.Server;
import com.mycompany.pa1c2gy.HC.Main.hcpGUI;
import com.mycompany.pa1c2gy.HC.Monitor.IEntranceHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IEvaluationHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IEntranceHall_CallCenter;
import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_ControlCentre;
import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_CallCenter;
import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.ICashier_Patient;


import com.mycompany.pa1c2gy.HC.Monitor.IEvaluationHall_Nurse;
import com.mycompany.pa1c2gy.HC.Monitor.MCallCenterHall;
import com.mycompany.pa1c2gy.HC.Monitor.MEvaluationHall;
import com.mycompany.pa1c2gy.HC.Monitor.MEntranceHall;

import com.mycompany.pa1c2gy.HC.Monitor.MWaitingRoomHall;
import com.mycompany.pa1c2gy.HC.Monitor.MWaitingHall;
import com.mycompany.pa1c2gy.HC.Monitor.MMedicalWaitingHall;
import com.mycompany.pa1c2gy.HC.Monitor.MMedicalRoomHall;
import com.mycompany.pa1c2gy.HC.Monitor.MPaymentHall;
import com.mycompany.pa1c2gy.HC.Monitor.MCashier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingRoomHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IMedicalWaitingHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IMedicalRoomHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingRoomHall_CallCenter;
import com.mycompany.pa1c2gy.HC.Monitor.IPaymentHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IPaymentHall_Cashier;
import com.mycompany.pa1c2gy.HC.Monitor.ICashier_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.ICashier_Cashier;


/**
 *
 * @author joaoc
 */
public class TControlCentre extends Thread implements IControlCentre{
    //private final String tE1Id;                    // Thread Id
    //private final IEntranceHall_Patient sr1;
    private Server server;
    private ICallCenterHall_ControlCentre mCallCenter = null;

    private boolean terminate;
    //private final IEvaluationHall_Patient sr2;
    
    public TControlCentre(Server server) {
        //this.tE1Id = tE1Id;
        //this.sr1 = sr1;
        this.server = server;        
        //this.iCallCenter = icc;
        this.terminate = false;
    }
    
    /**
      * Run the thread simulation, responsible to change the state of the simulation.
    */
    @Override
    public void run() {
        Socket s = null;
        System.out.println("CallCenter start");
        while(!terminate){
            try {
                s = this.server.receive();
            }
            catch(Exception e) {
                System.err.println(e);
            }
            new ClientControl(s).start();
        }
        this.server.close();
    }
    
    @Override
    public void startSimulation( int NoA, int NoC, int NoS) {
        mCallCenter = new MCallCenterHall(NoS);
        mCallCenter.start();
        MEntranceHall mEnH1 = new MEntranceHall(NoS);
        mEnH1.start();
        
        MEvaluationHall[] mEvH1 = new MEvaluationHall[4];
        
        MWaitingHall mWaH1 = new MWaitingHall(NoS);
        mWaH1.start();
        
        MWaitingRoomHall mWaRH1 = new MWaitingRoomHall(NoS);
        mWaRH1.start();
        
        MMedicalWaitingHall mMwH = new MMedicalWaitingHall(NoS);
        mMwH.start();
        
        MMedicalRoomHall[] mMrH = new MMedicalRoomHall[4];
        mMwH.start();
        
        MPaymentHall mPaH = new MPaymentHall(NoA+NoC);
        
        MCashier mCa = new MCashier();
        
        //mPaH.start();
        
        for (int i = 0; i < 4; i++) {
            mEvH1[i] = new MEvaluationHall(i, 1);
            TNurse nurse = new TNurse(i, (IEvaluationHall_Nurse)mEvH1[i]);
            nurse.start();
            
            mMrH[i] = new MMedicalRoomHall();
        }
        
        for(int i = 1; i<=NoC; i++){
                        String _id = "C"+String.valueOf(i);            
                        TPatient tE1 = new TPatient( _id, "C", (IEntranceHall_Patient)mEnH1, (ICallCenterHall_Patient) mCallCenter, (IEvaluationHall_Patient [])mEvH1, (IWaitingHall_Patient) mWaH1, (IWaitingRoomHall_Patient )mWaRH1, (IMedicalWaitingHall_Patient) mMwH, (IMedicalRoomHall_Patient[]) mMrH, (IPaymentHall_Patient) mPaH, (ICashier_Patient) mCa, "out");
                        tE1.start();
                        try{
                        Thread.sleep(500);
                       }
                        catch(Exception e){}
            }
        for(int i = 1; i<=NoA; i++){
            String _id = "A"+String.valueOf(i);
                    TPatient tE1 = new TPatient( _id, "A", (IEntranceHall_Patient)mEnH1, (ICallCenterHall_Patient) mCallCenter, (IEvaluationHall_Patient [])mEvH1, (IWaitingHall_Patient) mWaH1, (IWaitingRoomHall_Patient )mWaRH1, (IMedicalWaitingHall_Patient) mMwH, (IMedicalRoomHall_Patient[]) mMrH, (IPaymentHall_Patient) mPaH, (ICashier_Patient) mCa, "out");
                    tE1.start();
                    try{
                Thread.sleep(500);
               }
                catch(Exception e){}
           }

            
        TCashier cashier = new TCashier((IPaymentHall_Cashier) mPaH, (ICashier_Cashier) mCa);
        cashier.start();
        TCallCenter cc = new TCallCenter((IEntranceHall_CallCenter)mEnH1, (IWaitingRoomHall_CallCenter )mWaRH1, (ICallCenterHall_CallCenter)mCallCenter);
        cc.start();
    }

    @Override
    public void suspendSimulation() {
    }

    @Override
    public void resumeSimulation() {
    }

    @Override
    public void stopSimulation() {
        
    }
    
    @Override
    public void endSimulation() {
        hcpGUI.endSimulation();
    }
    
    @Override
    public void ccAllowMove(){
        mCallCenter.allowMove();
    }
    
    class ClientControl extends Thread{
    
        private final Socket socket;

        public ClientControl(Socket s){
            this.socket = s;

        }

        /**
        * Life cycle of the ClientThread Thread.
        */
        @Override
        public void run() {
            try {
                try (ObjectInputStream _in = new ObjectInputStream(socket.getInputStream())) {
                    String obj;
                    while ((obj = _in.readObject().toString()) != null){
                        System.out.println("Client Control: "+obj);
                        processMsg(obj);
                    }
                }

            } catch (IOException e) {
                System.err.println(e);                
                System.out.println("aqui");

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
                            startSimulation(NoA, NoC, NoS);
                        }
                        else if(msg.contains("Sus")){
                        }
                        else if(msg.contains("End")){
                            terminate = true;
                            endSimulation();
                        }
                        else if(msg.contains("Stop")){
                            stopSimulation();
                        }
                        else if(msg.contains("Allow")){
                            ccAllowMove();
                        }
                        else{
                            System.out.println("NOP");
                        }
        }
    }
}
