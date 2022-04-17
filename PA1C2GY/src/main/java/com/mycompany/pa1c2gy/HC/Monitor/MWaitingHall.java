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
    /** the simulation has stopped */
    private boolean stop;
    /** the simulation has ended */
    private boolean suspend;
    
    private int AdultsNumber;
    
    private int ChildrenNumber;    
    
    private int maxRoomNum;
    
    private int WTN;
    
    public MWaitingHall(int Patient_Num){
        rl = new ReentrantLock(true);
        Cleave = rl.newCondition();
        Aleave = rl.newCondition();
        this.AdultsNumber = 0;
        this.ChildrenNumber = 0;
        this.maxRoomNum = Patient_Num/2;
        WTN = 0;
    }
    
    public void start() {
        try{
            rl.lock();
            stop = false;
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
            suspend = true;
            Cleave.signal();
            Aleave.signal();
        } finally{
            rl.unlock();
        }
    }
    
    @Override
    public int incrWTN(){
        return WTN++;
    }
    
    @Override
    public String enter(String patientId) {

        String state = null;
        try{
            rl.lock();
            if(stop){return "Stop";}
            if(patientId.contains("C")){
                while(this.ChildrenNumber >= this.maxRoomNum || suspend)
                    Cleave.await();
                
                this.ChildrenNumber++;
            }
            if(patientId.contains("A")){
                while(this.AdultsNumber >= this.maxRoomNum || suspend)
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
