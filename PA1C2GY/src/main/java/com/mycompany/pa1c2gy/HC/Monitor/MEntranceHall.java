/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;
import com.mycompany.pa1c2gy.HC.Entities.TPatient;
//import com.mycompany.pa1c2gy.HC.FIFO.FIFO;
import com.mycompany.pa1c2gy.HC.FIFO.FIFO;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class MEntranceHall implements IEntranceHall_Patient, IEntranceHall_ControlCentre, IEntranceHall_CallCenter{
    
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    private final Condition leave;
    /** FIFO */
    private final FIFO fifo;
    /** the simulation has stopped */
    private boolean stop;
    /** the simulation has ended */
    private boolean suspend;
    
    private int AdultsNumber;
    
    private int ChildrenNumber;    
    
    
    // New state to patient when leaving
    private String new_state;

    
    public MEntranceHall(int Patient_Num){
        this.fifo = new FIFO(Patient_Num);
        rl = new ReentrantLock(true);
        leave = rl.newCondition();
        this.AdultsNumber = 0;
        this.ChildrenNumber = 0;
    }
    
    public void start() {
        try{
            rl.lock();
            stop = false;
            suspend = false;
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
            this.fifo.suspend();
            leave.signal();
        } finally{
            rl.unlock();
        }
    }
    
    public void resume() {
        try{
            rl.lock();
            suspend = false;
            this.fifo.resume();
            leave.signal();
        } finally{
            rl.unlock();
        }
    }
    @Override
    public String enter(String patientId) {
        if(patientId.contains("C")){
            this.ChildrenNumber++;
            
        }
        else{
            this.AdultsNumber++;
        }
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
            if(idOut.contains("C")){
                this.ChildrenNumber--;
            }
            else{
                this.AdultsNumber--;
            }
    }

}
