/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.StdFIFO;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class MMedicalWaitingHall implements IMedicalWaitingHall_Patient{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    private final Condition Cleave;
    private final Condition Aleave;
    /** the simulation has stopped */
    private boolean stop;
    private boolean suspend;
    private int AdultsNumber;
    
    private int ChildrenNumber;    
    
    
    private int[] numRoomPatients;
    
    
    public MMedicalWaitingHall(int Patient_Num){
        rl = new ReentrantLock(true);
        Cleave = rl.newCondition();
        Aleave = rl.newCondition();
        this.AdultsNumber = 0;
        this.ChildrenNumber = 0;
        numRoomPatients = new int[4];
        for(int i = 0; i < numRoomPatients.length; i++)
            numRoomPatients[0] = 0;
    }
    
    public void start() {
        try{
            rl.lock();
            stop = false;
            suspend = false;
        } finally{
            rl.unlock();
        }
    }
    
    public void stop() {
        try{
            rl.lock();
            stop = true;
        } finally{
            rl.unlock();
        }
    }
    
    public void suspend() {
        try{
            rl.lock();
            suspend = true;
            Cleave.signal();
            Aleave.signal();
        } finally{
            rl.unlock();
        }
    }
    
    public void resume() {
        try{
            rl.lock();
            suspend = false;
            Cleave.signal();
            Aleave.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public String enter(String patientId) {

        String state = null;
        try{
            rl.lock();
            System.out.println("IS in : C "+this.ChildrenNumber + " A "+this.AdultsNumber);
            if(patientId.contains("C")){
                while(numRoomPatients[0] == 1 && numRoomPatients[1] == 1 || suspend)
                    Cleave.await();
                
                if(numRoomPatients[0] == 0){state = "MedicalRoom_1";numRoomPatients[0]++;}
                else if(numRoomPatients[1] == 0){state = "MedicalRoom_2";numRoomPatients[1]++;}
                
                this.ChildrenNumber++;
            }
            if(patientId.contains("A")){
                while(numRoomPatients[2] == 1 && numRoomPatients[3] == 1 || suspend)
                    Aleave.await();
                
                if(numRoomPatients[2] == 0){state = "MedicalRoom_3";numRoomPatients[2]++;}
                else if(numRoomPatients[3] == 0){state = "MedicalRoom_4";numRoomPatients[3]++;}
                this.AdultsNumber++;
            }
        
        
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }

        return state;
    }
    
    @Override
    public void patientLeave(String type, int n){
        try{
            rl.lock();
            if(type.equals("C")){
                numRoomPatients[n]--;
                Cleave.signal();
                
            }
            if(type.equals("A")){
                numRoomPatients[n]--;
                Aleave.signal();
            }
        
        } finally{
            rl.unlock();
        }
    }
}
