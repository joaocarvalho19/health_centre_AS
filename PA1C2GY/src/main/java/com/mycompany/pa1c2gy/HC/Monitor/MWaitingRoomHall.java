/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.FIFO;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import com.mycompany.pa1c2gy.HC.FIFO.FIFO;
/**
 *
 * @author joaoc
 */
public class MWaitingRoomHall implements IWaitingRoomHall_Patient, IWaitingRoomHall_CallCenter{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    private final Condition leave;
    /** FIFO */
    private final FIFO fifo;
    /** the simulation has stopped */
    private boolean stop;
    private boolean suspend;
    
    private int AdultsNumber;
    
    private int ChildrenNumber;    
    
    private int maxRoomNum;
    
    private String new_state;
    
    
    public MWaitingRoomHall(int Patient_Num){
        this.fifo = new FIFO(Patient_Num);
        rl = new ReentrantLock(true);
        leave = rl.newCondition();
        this.AdultsNumber = 0;
        this.ChildrenNumber = 0;
        this.maxRoomNum = Patient_Num/2;
        new_state=null;
        
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
    
    public void stop() {
        try{
            rl.lock();
            stop = true;
            fifo.resetFIFO();
        } finally{
            rl.unlock();
        }
    }
    
    public void suspend() {
        try{
            rl.lock();
            suspend = true;
            leave.signal();
        } finally{
            rl.unlock();
        }
    }
    
    public void resume() {
        try{
            rl.lock();
            suspend = false;
            leave.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public String enter(String patientId) {

        String state = null;
        this.fifo.put(patientId);

        try{
            rl.lock();
            if(stop){return "Stop";}
            while(suspend)
                leave.await();
            state = new_state;
            
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }

        return state;
    }
    
    @Override
    public void allowPatient(String state) { 
        try{
            rl.lock();
            new_state = state;
            
        
        } finally{
            rl.unlock();
        }
        String idOut = this.fifo.get();

        this.fifo.printFIFIO();
    }

}
