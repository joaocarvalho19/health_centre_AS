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
public class MCashier implements ICashier_Patient, ICashier_Cashier{
    private int PYT;
    private final ReentrantLock rl;
    private StdFIFO fifo;
    private boolean stop;
    private boolean suspend;
    private int numPatients;
    private final Condition leave;

        
    public MCashier(int PYT){
        this.fifo = new StdFIFO(1);
        this.rl = new ReentrantLock(true);
        this.suspend = false;
        this.stop = false;
        this.PYT = PYT;
        numPatients = 0;
        leave = rl.newCondition();
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
    public String pay(String patientId) {
        String state = null;
        numPatients++;
        this.fifo.put(patientId);
        try{
            rl.lock();
            if(stop){return "Stop";}
            while(suspend)
                leave.await();
            state = "Finish";
            
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }
        numPatients--;
        return state;    }

    @Override
    public void acceptPayment() {
        try {
            Thread.sleep(PYT);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        try{
                rl.lock();
                
        } finally{
            rl.unlock();
        }
        
        this.fifo.get();
    }
    
    @Override
    public boolean hasPatient(){
        return (numPatients > 0);
    }
    
}
