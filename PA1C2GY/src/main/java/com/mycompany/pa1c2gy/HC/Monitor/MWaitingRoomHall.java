/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.TESTFIFO;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import com.mycompany.pa1c2gy.HC.FIFO.TESTFIFO;
/**
 *
 * @author joaoc
 */
public class MWaitingRoomHall implements IWaitingRoomHall_Patient, IWaitingRoomHall_CallCenter{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    private final Condition notFull;
    /** FIFO */
    private final TESTFIFO fifo;
    /** the simulation has stopped */
    private boolean stop;
    /** the simulation has ended */
    private boolean end;
    
    private int AdultsNumber;
    
    private int ChildrenNumber;    
    
    private int maxRoomNum;
    
    private String new_state;
    
    
    public MWaitingRoomHall(int Patient_Num){
        this.fifo = new TESTFIFO(Patient_Num);
        rl = new ReentrantLock(true);
        notFull = rl.newCondition();
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

    @Override
    public String enter(String patientId) {

        String state = null;
        System.out.println("IN");
        this.fifo.put(patientId);

        try{
            rl.lock();

            state = new_state;
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

        System.out.println("GET: "+idOut);
        this.fifo.printFIFIO();
    }

}
