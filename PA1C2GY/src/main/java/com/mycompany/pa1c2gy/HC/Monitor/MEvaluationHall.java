/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.EvFIFO;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.Random;

/**
 *
 * @author joaoc
 */
public class MEvaluationHall implements IEvaluationHall_Patient, IEvaluationHall_CallCenter, IEvaluationHall_Nurse{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    private Condition leave;
    /** FIFO */
    private final EvFIFO fifo;
    
    private boolean stop;
    private boolean suspend;
    private boolean arrived;
    private int Id;
    private final String[] DoSList;
    private int WTN;
    private final int EVT;


    
    // New state to patient
    private String new_state;

    
    public MEvaluationHall(int Id, int numPatients, int EVT){
        this.fifo = new EvFIFO(numPatients);
        rl = new ReentrantLock(true);
        leave = rl.newCondition();
        this.arrived = false;
        this.Id = Id;
        this.EVT = EVT;
        DoSList = new String[3];
        DoSList[0] = "B";
        DoSList[1] = "Y";
        DoSList[2] = "R";
        WTN = 1;
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
            leave.signal();
        } finally{
            rl.unlock();
        }
    }
    
    public void resume() {
        try{
            rl.lock();
            suspend = true;
            leave.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public String enter(String patientId) {
        String state = null;
        this.arrived = true;
        this.fifo.put(patientId);
        
        this.arrived = false;
        try{
            rl.lock();
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
    public void evaluate() {
        try {
            Thread.sleep(this.EVT);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        try{
            rl.lock();
            String Id = this.fifo.get();
            System.out.println("EVAL GET: "+Id);
            
            Random r = new Random();
            int randomitem = r.nextInt(DoSList.length);
            String randomColor = DoSList[randomitem];
            if(Id.contains("C")){
                new_state = "CWaiting-"+randomColor;
            }
            if(Id.contains("A")){
                new_state = "AWaiting-"+randomColor;
            }
            
        } finally{
            rl.unlock();
        }
   
    }
    
    @Override
    public boolean patientArrived() {
        return arrived;
    }
    
    @Override
    public int getWTN(){
        return WTN++;
    }
    
}
