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
import com.mycompany.pa1c2gy.HC.Monitor.ICashier_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IMedicalRoomHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IWaitingRoomHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IMedicalWaitingHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IPaymentHall_Patient;
import javax.swing.JList;
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
    private int WTN;
    private int TtMove;
    private final IEvaluationHall_Patient[] iEvaluationHall;
    private final IWaitingRoomHall_Patient iwaitingRoomHall;
    private final IWaitingHall_Patient iwaitingHall;
    private final IMedicalWaitingHall_Patient imedicalWaitingHall;
    private final IPaymentHall_Patient ipaymentHall;
    private final ICashier_Patient icashier;
    private final IMedicalRoomHall_Patient[] imedicalRoomHall;
    javax.swing.JList evalRoom;
    javax.swing.JList medicalRoom;
    
    
    public TPatient(String tE1Id, String type, int TtMove, IEntranceHall_Patient sr1, ICallCenterHall_Patient callCenter, IEvaluationHall_Patient[] evaluationHall, IWaitingHall_Patient iwaitingHall, IWaitingRoomHall_Patient iwaitingRoomHall, IMedicalWaitingHall_Patient imedicalWaitingHall, IMedicalRoomHall_Patient[] imedicalRoomHall, IPaymentHall_Patient ipaymentHall, ICashier_Patient icashier, String state) {
        this.patientId = tE1Id;
        this.type = type;
        this.iEntranceHall = sr1;
        this.state = "EntranceHall";
        this.iEvaluationHall = evaluationHall;
        this.iwaitingHall = iwaitingHall;
        this.iwaitingRoomHall = iwaitingRoomHall;
        this.imedicalWaitingHall = imedicalWaitingHall;
        this.imedicalRoomHall = imedicalRoomHall;
        this.ipaymentHall = ipaymentHall;
        this.icashier = icashier;
        this.icallCenter = callCenter;
        this.TtMove = TtMove;
        evalRoom = null;
        medicalRoom = null;
        DoS = null;
        WTN = 0;
 
    }
    
    @Override
    public void run() {
        // state machine for the Patient (Thread)
        
        System.out.println("Create Patient with Id: "+this.patientId);
        //Enter in simulation
        hcpGUI.appendPatient(hcpGUI.firstList, this.patientId);
        try{
            Thread.sleep(TtMove);
        }
        catch(Exception e){}
        
        while (true) {

            if(state.equals("Stop")){
                System.out.println("Stoping");
                break;
            }
            if(state.equals("EntranceHall")){
                    
                
                if(this.type.equals("C")){
                    hcpGUI.moveCostumer(hcpGUI.firstList, hcpGUI.entranceChildrenList, patientId, patientId);
                }
                if(this.type.equals("A")){
                    hcpGUI.moveCostumer(hcpGUI.firstList, hcpGUI.entranceAdultsList, patientId, patientId);
                }
                icallCenter.updateEntranceSlots(1);
                
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = iEntranceHall.enter(patientId); //This state has the eval romm
                System.out.println(this.patientId+" : "+this.state);
                icallCenter.updateEntranceSlots(-1);
            }
            if(state.equals("EvHall_1") || state.equals("EvHall_2") || state.equals("EvHall_3")|| state.equals("EvHall_4")){
                int evaRoom_num = Integer.parseInt(state.split("_")[1]);

                switch(evaRoom_num){
                    
                    case 1: 
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList1, this.patientId, patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList1, this.patientId, patientId);}
                        evalRoom = hcpGUI.evaluationList1;
                        break;
                    case 2:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList2, this.patientId, patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList2, this.patientId, patientId);}
                        evalRoom = hcpGUI.evaluationList2;
                        break;
                    case 3:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList3, this.patientId, patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList3, this.patientId, patientId);}
                        evalRoom = hcpGUI.evaluationList3;
                        break;
                    case 4:
                        if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.entranceChildrenList, hcpGUI.evaluationList4, this.patientId, patientId);}
                        if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.entranceAdultsList, hcpGUI.evaluationList4, this.patientId, patientId);}
                        evalRoom = hcpGUI.evaluationList4;
                        break; 
                }
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = iEvaluationHall[evaRoom_num-1].enter(this.patientId);

                System.out.print(state+this.patientId);
                icallCenter.evaluationHallFreeSlot(evaRoom_num-1);
            }
            if(state.contains("CWaiting") || state.contains("AWaiting")){
                DoS = state.split("-")[1];
                
                String newPatientId = patientId+DoS;
                hcpGUI.moveCostumer(evalRoom, hcpGUI.waitingHallList, this.patientId, newPatientId);
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = iwaitingHall.enter(patientId);
                this.patientId = newPatientId;

            }
            
                //  Waiting Rooms
            if (state.equals("WaitingRoom")){
                    System.out.println("Patient enter in waiting room");
                    WTN = iwaitingHall.incrWTN();
                    String newPatientId = patientId+WTN;
                    if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.waitingHallList, hcpGUI.childrenWaitingList, patientId, newPatientId);}
                    if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.waitingHallList, hcpGUI.adultsWaitingList, patientId, newPatientId);}
                    icallCenter.updateWaitingSlots();
                    this.patientId = newPatientId;
                    
                    try{
                        Thread.sleep(this.TtMove);
                    }
                    catch(Exception e){}
                    state = iwaitingRoomHall.enter(patientId);
                    
            }
                
            
            if (state.equals("MedicalWait")){
                System.out.println("Patient enter in medical wait hall");
                
                icallCenter.updateMedicalHallSlots(type, 1);
                iwaitingHall.patientLeave(type);
                if(this.type.equals("C")){hcpGUI.moveCostumer(hcpGUI.childrenWaitingList, hcpGUI.medicalWaitList, patientId, patientId);}
                if(this.type.equals("A")){hcpGUI.moveCostumer(hcpGUI.adultsWaitingList, hcpGUI.medicalWaitList, patientId, patientId);}
                
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = imedicalWaitingHall.enter(patientId);
                System.out.println(this.patientId+" Medical: "+this.state);

            }
            
            if(state.equals("MedicalRoom_1") || state.equals("MedicalRoom_2") || state.equals("MedicalRoom_3")|| state.equals("MedicalRoom_4")){
                icallCenter.updateMedicalHallSlots(type, -1);
                int medicalRoom_num = Integer.parseInt(state.split("_")[1]);

                switch(medicalRoom_num){
                    
                    case 1: 
                        hcpGUI.moveCostumer(hcpGUI.medicalWaitList, hcpGUI.medicalRoomList1, this.patientId, patientId);
                        medicalRoom = hcpGUI.medicalRoomList1;
                        break;
                    case 2:
                       hcpGUI.moveCostumer(hcpGUI.medicalWaitList, hcpGUI.medicalRoomList2, this.patientId, patientId);
                        medicalRoom = hcpGUI.medicalRoomList2;
                        break;
                    case 3:
                        hcpGUI.moveCostumer(hcpGUI.medicalWaitList, hcpGUI.medicalRoomList3, this.patientId, patientId);
                        medicalRoom = hcpGUI.medicalRoomList3;
                        break;
                    case 4:
                        hcpGUI.moveCostumer(hcpGUI.medicalWaitList, hcpGUI.medicalRoomList4, this.patientId, patientId);
                        medicalRoom = hcpGUI.medicalRoomList4;
                        break; 
                }     
                
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = imedicalRoomHall[medicalRoom_num-1].enter(this.patientId);
                imedicalWaitingHall.patientLeave(type, medicalRoom_num-1);
                System.out.print(state+this.patientId);
            }
            
            if (state.equals("PaymentHall")){
                System.out.println("Patient enter in Payment hall");
                hcpGUI.moveCostumer(medicalRoom, hcpGUI.paymentList, patientId, patientId);
                
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = ipaymentHall.enter(patientId);
                System.out.print("OUT PAY : "+state+this.patientId);
            }
            
            if (state.equals("Cashier")){
                System.out.println("Patient enter in Payment hall");
                hcpGUI.moveCostumer(hcpGUI.paymentList, hcpGUI.cashierList, patientId, patientId);
                
                try{
                    Thread.sleep(this.TtMove);
                }
                catch(Exception e){}
                state = icashier.pay(patientId);
                System.out.println("Patient "+patientId+"has finished!");
                hcpGUI.removePatient(hcpGUI.cashierList, patientId);
            }
        }
        
    }

}
