/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Thread representing one Entity
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.IEntranceHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IEvaluationHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingHall_Patient;

import com.mycompany.pa1c2gy.HC.Main.hcpGUI;
/**
 *
 * @author user
 */
public class TPatient extends Thread {

    private String patientId;                    // Thread Id
    private String type;                        // Children(C) / Adult(A) 
    private final IEntranceHall_Patient iEntranceHall;
    private final ICallCenterHall_Patient icallCenter;
    private String state;
    private String DoS;
    private final IEvaluationHall_Patient[] iEvaluationHall;
    private final IWaitingHall_Patient iwaitingHall;
    javax.swing.JList evalRoom;
    
    //private final IEvaluationHall_Patient sr2;
    
    
    public TPatient(String tE1Id, String type, IEntranceHall_Patient sr1, ICallCenterHall_Patient callCenter, IEvaluationHall_Patient[] evaluationHall, IWaitingHall_Patient iwaitingHall, String state) {
        this.patientId = tE1Id;
        this.type = type;
        this.iEntranceHall = sr1;
        this.state = "EntranceHall";
        this.iEvaluationHall = evaluationHall;
        this.iwaitingHall = iwaitingHall;
        this.icallCenter = callCenter;
        evalRoom = null;
        DoS = null;
 
    }
    
    @Override
    public void run() {
        // state machine for the Patient (Thread)
        
        System.out.println("Create Patient with Id: "+this.patientId);
        //Enter in simulation
        hcpGUI.appendPatient(hcpGUI.firstList, this.patientId, DoS);
        try{
            Thread.sleep(100);
        }
        catch(Exception e){}
        
        //Enter in Entrance Hall
        while (true) {
            /*if(this.state.equals("OUT")){
                if(!iEntranceHall.entranceFull(type)){
                    state = "EntranceHall";
                    System.out.println(patientId);
                    System.out.println(this.patientId+" : "+this.state);
                }
            }*/
            
            if(state.equals("EntranceHall")){
                    
                
                if(this.type.equals("C")){
                    hcpGUI.moveCostumer(hcpGUI.firstList, hcpGUI.entranceChildrenList, patientId, DoS);
                }
                if(this.type.equals("A")){
                    hcpGUI.moveCostumer(hcpGUI.firstList, hcpGUI.entranceAdultsList, patientId, DoS);
                }
                icallCenter.updateEntranceSlots(1);
                state = iEntranceHall.enter(patientId); //This state has the eval romm
                System.out.println(this.patientId+" : "+this.state);
                icallCenter.updateEntranceSlots(-1);
            }
            //Evaluationstate = iEvaluationHall[evaRoom_num-1].enter(this.patientId);
            if(state.equals("EvHall_1") || state.equals("EvHall_2") || state.equals("EvHall_3")|| state.equals("EvHall_4")){
                int evaRoom_num = Integer.parseInt(state.split("_")[1]);
                //System.out.println("ROOM TO EVAL: "+evaRoom_num);

                switch(evaRoom_num){
                    
                    case 1: 
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList1, this.patientId, DoS);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList1, this.patientId, DoS);}
                        evalRoom = hcpGUI.evaluationList1;
                        break;
                    case 2:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList2, this.patientId, DoS);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList2, this.patientId, DoS);}
                        evalRoom = hcpGUI.evaluationList2;
                        break;
                    case 3:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList3, this.patientId, DoS);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList3, this.patientId, DoS);}
                        evalRoom = hcpGUI.evaluationList3;
                        break;
                    case 4:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList4, this.patientId, DoS);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList4, this.patientId, DoS);}
                        evalRoom = hcpGUI.evaluationList4;
                        break; 
                }     
                state = iEvaluationHall[evaRoom_num-1].enter(this.patientId);

                System.out.print(state+this.patientId);
                icallCenter.evaluationHallFreeSlot(evaRoom_num-1);
                
            }
            if(state.contains("CWaiting") || state.contains("AWaiting")){
                DoS = state.split("-")[1];
                
                //if(state.contains("CWaiting")){hcpGUI.moveCostumer(evalRoom, hcpGUI.childrenWaitingList, this.patientId, DoS);}
                //if(state.contains("AWaiting")){hcpGUI.moveCostumer(evalRoom, hcpGUI.adultsWaitingList, this.patientId, DoS);}
                hcpGUI.moveCostumer(evalRoom, hcpGUI.waitingHallList, this.patientId, DoS);
                icallCenter.updateWaitingSlots();
                this.patientId = patientId+DoS;
                state = iwaitingHall.enter(patientId); //This state has the eval romm
                
                System.out.println(this.patientId+" WAITING: "+this.state);
                state=" ";
            }
        }
        
    }

}
