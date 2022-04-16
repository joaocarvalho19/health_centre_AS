/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IEntranceHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IEvaluationHall_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.IEvaluationHall_Nurse;


/**
 *
 * @author joaoc
 */
public class TNurse extends Thread{
    private final IEvaluationHall_Nurse iEvaluation;
    private final int Id;
    //private final IEvaluationHall_Patient sr2;
    
    
    public TNurse(int Id, IEvaluationHall_Nurse iEvaluation) {
        this.iEvaluation = iEvaluation;
        this.Id = Id;
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
            if(iEvaluation.patientArrived()){
                System.out.println("Nurse: "+Id+iEvaluation.patientArrived());
                iEvaluation.evaluate();
                //break;
            }
        }
    }
}
