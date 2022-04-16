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
public class MWaitingHall implements IWaitingHall_Patient{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    private final Condition Cleave;
    private final Condition Aleave;
    /** FIFO */
    private final StdFIFO fifo;
    /** the simulation has stopped */
    private boolean stop;
    /** the simulation has ended */
    private boolean end;
    
    private int AdultsNumber;
    
    private int ChildrenNumber;    
    
    private int maxRoomNum;
    
    private boolean allow;
    
    private String new_state;
    
    public MWaitingHall(int Patient_Num){
        this.fifo = new StdFIFO(Patient_Num);
        rl = new ReentrantLock(true);
        Cleave = rl.newCondition();
        Aleave = rl.newCondition();
        this.AdultsNumber = 0;
        this.ChildrenNumber = 0;
        this.maxRoomNum = Patient_Num/2;
        allow = false;
    }
    
    public void start() {
        try{
            rl.lock();
            stop = false;
            fifo.resetFIFO();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public String enter(String patientId) {

        String state = null;
        //this.fifo.put(patientId);
        try{
            rl.lock();
            System.out.println("IS WAITING: C "+this.ChildrenNumber + " A "+this.AdultsNumber);
            if(patientId.contains("C")){
                while(this.ChildrenNumber >= this.maxRoomNum)
                    Cleave.await();
                
                this.ChildrenNumber++;
            }
            if(patientId.contains("A")){
                while(this.AdultsNumber >= this.maxRoomNum)
                    Aleave.await();
                
                this.AdultsNumber++;
            }
            state = "WaitingRoom";
        
        
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }

        return state;
    }
    
    @Override
    public void patientLeave(String type){
        try{
            rl.lock();
            if(type.equals("C")){
                this.ChildrenNumber--;
                Cleave.signal();
                
            }
            if(type.equals("A")){
                this.AdultsNumber--;
                Aleave.signal();
            }
        
        } finally{
            rl.unlock();
        }
    }
    
}
