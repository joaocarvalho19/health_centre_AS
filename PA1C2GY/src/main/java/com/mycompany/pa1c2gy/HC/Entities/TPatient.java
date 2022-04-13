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

import com.mycompany.pa1c2gy.HC.Main.hcpGUI;
/**
 *
 * @author user
 */
public class TPatient extends Thread {

    private final String patientId;                    // Thread Id
    private String type;                        // Children(C) / Adult(A) 
    private final IEntranceHall_Patient iEntranceHall;
    private final ICallCenterHall_Patient callCenter;
    private String state;
    private final IEvaluationHall_Patient[] iEvaluationHall;
    
    //private final IEvaluationHall_Patient sr2;
    
    
    public TPatient(String tE1Id, String type, IEntranceHall_Patient sr1, ICallCenterHall_Patient callCenter, IEvaluationHall_Patient[] evaluationHall, String state) {
        this.patientId = tE1Id;
        this.type = type;
        this.iEntranceHall = sr1;
        this.state = "EntranceHall";
        this.iEvaluationHall = evaluationHall;
        this.callCenter = callCenter;
 
    }
    
    @Override
    public void run() {
        // state machine for the Patient (Thread)
        
        System.out.println("Create Patient with Id: "+this.patientId);
        //Enter in simulation
        hcpGUI.appendPatient(hcpGUI.firstList, this.patientId);
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
                    hcpGUI.moveCostumer(hcpGUI.firstList, hcpGUI.entranceChildrenList, patientId);
                    callCenter.entranceHallAdultFreeSlot();
                }
                if(this.type.equals("A")){
                    hcpGUI.moveCostumer(hcpGUI.firstList, hcpGUI.entranceAdultsList, patientId);
                    callCenter.entranceHallChildrenFreeSlot();
                }
                state = iEntranceHall.enter(patientId); //This state has the eval romm
                
                System.out.println(this.patientId+" : "+this.state);
            }
            //Evaluationstate = iEvaluationHall[evaRoom_num-1].enter(this.patientId);
            if(state.equals("EvHall_1") || state.equals("EvHall_2") || state.equals("EvHall_3")|| state.equals("EvHall_4")){
                int evaRoom_num = Integer.parseInt(state.split("_")[1]);
                //System.out.println("ROOM TO EVAL: "+evaRoom_num);

                //state = iEvaluationHall[evaRoom_num-1].enter(this.patientId);
                System.out.print(state+this.patientId);
                System.out.println(evaRoom_num);
                switch(evaRoom_num){
                    
                    case 1: 
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList1, this.patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList1, this.patientId);}
                        break;
                    case 2:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList2, this.patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList2, this.patientId);}
                        break;
                    case 3:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList3, this.patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList3, this.patientId);}
                        break;
                    case 4:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList4, this.patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList4, this.patientId);}
                        break;
                }
                
                break;
            }
        }
        
    }

}
